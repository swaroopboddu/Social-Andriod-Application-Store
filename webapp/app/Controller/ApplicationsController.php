<?php
App::uses('AppController', 'Controller');
App::uses('CakeEmail', 'Network/Email');
App::uses('HttpSocket', 'Network/Http');
/**
 * Applications Controller
 *
 * @property Application $Application
 * @property PaginatorComponent $Paginator
 * @property SessionComponent $Session
 */
class ApplicationsController extends AppController {

/**
 * Components
 *
 * @var array
 */
	public $components = array('Paginator', 'Session');
	public $uses = array('Application', 'ApplicationRevision',
	 'UserApplication', 'User', 'GroupsUser','UserFollower',
	 'UserFriend','GroupApplication','Comment');


	public function beforeFilter() {
		parent::beforeFilter();
		//$this->Auth->allow('add');
		$this->Auth->allow('download_app');
}

/**
 * index method
 *
 * @return void
 */
	public function index() {
		$this->Application->recursive = 0;
		$headers = apache_request_headers();
		if(isset($headers['token'])) {
			//Return token --> users list of applications
			$token = $headers['token'];
				$user = $this->User->find('first', array(
					'conditions' => array('User.token' => $token),
					'fields' => array('User.id'),
					'recursive' => 0));
				if(isset($user['User']['id'])) {
					//Check if params is set -- List of apps for passed UserID
					if(isset($this->request->params['pass'][0])) {
						$conditions = array('User.id' => $this->request->params['pass'][0]);
						if($this->User->hasAny($conditions)) {
							$result = $this->UserApplication->find('all', array(
										'conditions' => array('UserApplication.user_id' => $this->request->params['pass'][0]),
										'recursive' => 2));
							//pr($result);
							$this->set('result',$result);
							$this->set('_serialize', array('result'));
						} else {
							$result = "Invalid User ID";
							$this->set('result',$result);
							$this->set('_serialize', array('result'));
						}
					} else { 
						$result = $this->UserApplication->find('all', array(
						'conditions' => array('UserApplication.user_id' => $user['User']['id']),
						'recursive' => 2));
						$this->set('result',$result);
						$this->set('_serialize', array('result'));
					}
				} else {
					//Invalid token
					$result = "Invalid token id";
					$this->set('_serialize', array($result));
				}

		} else {
			//return generic list of apps

			$this->set('applications', $this->Paginator->paginate());
			$this->set('result', $this->Paginator->paginate());
			$this->set('_serialize', array('result'));
		}
	}

/**
 * view method
 *
 * @throws NotFoundException
 * @param string $id
 * @return void
 */
	public function view($id = null) {
		if (!$this->Application->exists($id)) {
			throw new NotFoundException(__('Invalid application'));
		}
		$options = array('conditions' => array('Application.' . $this->Application->primaryKey => $id));
		$this->set('application', $this->Application->find('first', $options));
		$comments = $this->Comment->find('all', array(
		 	'conditions' => array('comment.application_id' => $id),
		 	'order' => array('comment.id DESC')));
		$this->Set('comments', $comments);
	}

/**
 * add method
 *
 * @return void
 */
	public function add() {
			if ($this->request->is('post')) {
				//Save the uploaded file under webroot
				$upload_file_array = $this->request->data['Application']['file'];
				if((isset($upload_file_array['error']) && $upload_file_array['error'] == 0)
					|| (!empty( $upload_file_array['tmp_name']) && $upload_file_array['tmp_name'] != 'none')) {
				$now  = date('Y-m-d-His');
				$name = str_replace(' ', '_', $upload_file_array['name']);
				$dest_file_path  = "".$now.$name."_"."1.0";
				$did_move_succeed = move_uploaded_file( $upload_file_array['tmp_name'], APP."webroot/uploads/".$dest_file_path);
				if($did_move_succeed !== 0) {
					$this->Session->setFlash(__('file upload failed.. Please, try again.'));
				}

				//pr($dest_file_path);

				//Save the parameters in application table
				$this->request->data['Application']['user_id'] = $this->Session->read('User.id');
				$this->request->data['Application']['path'] = APP."webroot/uploads/".$dest_file_path;
				if ($this->Application->save($this->request->data)) {
					$new_app_id = $this->Application->getLastInsertId();
					$application_revision = array(
						'ApplicationRevision' => array(
							'app_id' => $new_app_id,
							'revision_number' => "1.0.0",
							'path' => $dest_file_path,
							'size' => $upload_file_array['size'],
							'filename' => $upload_file_array['name'],
							)
						);
					//pr($application_revision);
					if($this->ApplicationRevision->save($application_revision))
					{
						$this->Session->setFlash(__('Your application has been saved.'));
						//Send email to his followers - new app has been uploaded

						$followers = $this->UserFollower->find('all', array(
							'conditions' => array('UserFollower.follower_user_id' => $this->Session->read('User.id'))));
						if(!empty($followers)) {
							foreach ($followers as $follower) {
								$user = $this->User->find('first', array('User.id' => $follower['UserFollower']['user_id']));
								if(!empty($user)) {
									$Email = new CakeEmail();
									$Email->emailFormat('html');
									$Email->template('default');
									//$Email->format('html');
									$Email->from(array('NO-REPLY@androidgeek.vlab.asu.edu' => 'Secure Social App Store'));
									$Email->to('avinash.vllbh@gmail.com');
									$subject = $this->Session->read('User.first_name')." ".$this->Session->read('User.last_name')." has just uploaded a new app!!!";
									$Email->subject($subject);

									$message = "<div class=\"container\">Hello ".$user['User']['first_name']." ".
												$user['User']['last_name']."<br>".
												"<div style=\"margin:10px\"><b><i>".
												$this->Session->read('User.first_name').
												" ".$this->Session->read('User.last_name')."</i></b> has uploaded a new application<br><br><br>".
												"<div style=\"margin:10px\"><b>".$this->request->data['Application']['title']."</b><br>".
												"<p>".$this->request->data['Application']['description']."</p>".
												"</div></div></div>";
									$Email->send($message);

									//Send a GCM Push notification to the user
									$HttpSocket = new HttpSocket();
									$options = array(
									  'header' => array(
									    'Content-Type' => 'application/json',
									    'Authorization' => 'key=AIzaSyBEU26a09YUPirxWI0JgfMMM30Ubr8uwms'
									  )
									);    
									$data = array("time_to_live" => 108,
											  		"data"=>array(
														"Message" => $subject,
														"type" => 'application',
														'id' => $new_app_id
													),
											  "registration_ids" => array($user['User']['registration_id'])
											);
									$HttpSocket->post('https://android.googleapis.com/gcm/send', $data, $options);
								}
							}
						}
						$this->redirect(array('controller' => 'users', 'action' => 'index'));
					}
					else{
						$this->Session->setFlash(__('The application could not be saved. Please, try again.'));
					}
				} else {
					$this->Session->setFlash(__('The application could not be saved. Please, try again.'));
				}
			}
		}
	}

/**
 * edit method
 *
 * @throws NotFoundException
 * @param string $id
 * @return void
 */
	public function edit($id = null) {
		if (!$this->Application->exists($id)) {
			throw new NotFoundException(__('Invalid application'));
			$this->Session->setFlash("Invalid application");
			$this->redirect(array('controller'=>'users','action'=>'index'));
		}
		//Check if it's a post
		if ($this->request->is(array('post', 'put'))) {

				$upload_file_array = $this->request->data['Application']['file'];
				if((isset($upload_file_array['error']) && $upload_file_array['error'] == 0)
					|| (!empty( $upload_file_array['tmp_name']) && $upload_file_array['tmp_name'] != 'none')) {
				$now  = date('Y-m-d-His');
				$name = str_replace(' ', '_', $upload_file_array['name']);
				$dest_file_path  = "".$now.$name."_"."1.0";
				$did_move_succeed = move_uploaded_file( $upload_file_array['tmp_name'], APP."webroot/uploads/".$dest_file_path);
				if($did_move_succeed !== 0) {
					$this->Session->setFlash(__('file upload failed.. Please, try again.'));
				}

				$rev_num = $this->request->data['Application']['revision_number'];
				unset($this->request->data['Application']['revision_number']);
				$this->request->data['Application']['path'] = APP."webroot/uploads/".$dest_file_path;
				if ($this->Application->save($this->request->data)) {
					$application_revision = array(
						'ApplicationRevision' => array(
							'app_id' => $this->request->data['Application']['id'],
							'revision_number' => $rev_num,
							'path' => $dest_file_path,
							'size' => $upload_file_array['size'],
							'filename' => $upload_file_array['name'],
							)
						);
					//pr($application_revision);
					if($this->ApplicationRevision->save($application_revision))
					{
						$this->Session->setFlash(__('Your application has been saved.'));
						$this->redirect(array('controller' => 'users', 'action' => 'index'));
					}
					else{
						$this->Session->setFlash(__('The application could not be saved. Please, try again.'));
					}
				} else {
					$this->Session->setFlash(__('The application could not be saved. Please, try again.'));
				}
			}
			else
			{
				unset($this->request->data['Application']['revision_number']);
				if ($this->Application->save($this->request->data)) {
					$this->Session->setFlash(__('Your application has been saved.'));
					$this->redirect(array('controller' => 'users', 'action' => 'index'));
				}
				else {
					$this->Session->setFlash(__('The application could not be saved. Please, try again.'));
				}	
			}
		} 
		//Pass the application parameters that need to be edited
		else {
			$options = array('conditions' => array('Application.' . $this->Application->primaryKey => $id));
			$this->request->data = $this->Application->find('first', $options);
		}
	}

/**
 * delete method
 *
 * @throws NotFoundException
 * @param string $id
 * @return void
 */
	public function delete($id = null) {
		$this->Application->id = $id;
		if (!$this->Application->exists()) {
			throw new NotFoundException(__('Invalid application'));
		}
		$this->request->onlyAllow('post', 'delete');
		if ($this->Application->delete()) {
			$this->Session->setFlash(__('The application has been deleted.'));
		} else {
			$this->Session->setFlash(__('The application could not be deleted. Please, try again.'));
		}
		return $this->redirect(array('action' => 'index'));
	}

/**
* Method to download the file
*/

	public function download_app($id = null) {
		$this->Application->recursive = -1;
		if($id != null) {
			$headers = apache_request_headers();
			if(isset($headers['token'])) {
				// if($this->request->params['ext'] == "json") {
					$token = $headers['token'];
					$user = $this->User->find('first', array(
					'conditions' => array('User.token' => $token),
					'recursive' => 0));
					if(isset($user['User']['id'])) {
						$app = $this->Application->find('first', array('conditions'=>array('Application.id' => $id)));
						$name = $this->ApplicationRevision->find('first', 
											array('conditions' => array('ApplicationRevision.app_id' => $id),
													'order' => array('ApplicationRevision.id' => 'desc')));
						//pr($name);
						//apache_response_headers('Content-Type') = 'application/vnd.android.package-archive'; 
						$this->response->file($app['Application']['path'], array(
							'download' => true, 'name' => $name['ApplicationRevision']['filename']));

						//Record the download against the user
						$user_app = array();
						$user_app['user_id'] = $user['User']['id'];
						$user_app['application_id'] = $app['Application']['id'];
						$user_app['downloaded_on'] = date('Y-m-d H:i:s');
						$record = $this->UserApplication->find('first', array(
							'conditions' => array('UserApplication.user_id' => $user_app['user_id'],
												'UserApplication.application_id' => $user_app['application_id'])));
						if(isset($record['UserApplication']['id'])) {
							//Do not save
						} else {
							$this->UserApplication->create();
							$this->UserApplication->save($user_app);
						}
						//Check if his friends have tried that application or not
						$friends = $this->UserFriend->find('all', array(
							'conditions' => array('UserFriend.user_id' => $user['User']['id'], 'UserFriend.status' => 0),
							'recursive' => 0));
						if(!empty($friends)){
							foreach ($friends as $friend) {
								$friend_detail = $this->User->find('first', array('conditions' => array(
									'User.id' => $friend['UserFriend']['friend_user_id'])));
								//Check if his friend has already downloaded that app
								$check_download_condition = array('UserApplication.user_id' => $friend_detail['User']['id'],
																	'UserApplication.application_id' => $app['Application']['id']);
								if($this->UserApplication->hasAny($check_download_condition)){
									//Do Nothing
								} else {
									if(!empty($friend_detail)) {
										$Email = new CakeEmail();
										$Email->emailFormat('html');
										$Email->template('default');
										//$Email->format('html');
										$Email->from(array('NO-REPLY@androidgeek.vlab.asu.edu' => 'Secure Social App Store'));
										$Email->to($friend_detail['User']['email']);

										$Email->subject('Your friend has just downloaded a new App!!!');
										$message = "<div class=\"container\">Hello ".$friend_detail['User']['first_name']." ".
													$friend_detail['User']['last_name']."<br>".
													"<div style=\"margin:10px\">Your friend <b><i>".
													$user['User']['first_name']." ".$user['User']['last_name'].
													"</i></b> has just downloaded ".$app['Application']['title']."<br><br>".
													"<div style=\"margin:5px\"><b>".$app['Application']['title']."</b><br>".
													"<p>".$app['Application']['description']."</p>".
													"</div></div></div>";
										$Email->send($message);

										//Send a push notification
										$HttpSocket = new HttpSocket();
										$options = array(
										  'header' => array(
										    'Content-Type' => 'application/json',
										    'Authorization' => 'key=AIzaSyBEU26a09YUPirxWI0JgfMMM30Ubr8uwms'
										  )
										);    
										$data = array("time_to_live" => 108,
												  		"data"=>array(
															"Message" => "Your friend has just downloaded a new App!!!",
															"type" => 'application',
															'id' => $app['Application']['id']
														),
												  "registration_ids" => array($friend_detail['User']['registration_id'])
												);
										$HttpSocket->post('https://android.googleapis.com/gcm/send', $data, $options);
									}
								}
							}
						}
						//Add the application to the count of downloads in the groups user belongs to
						$user_groups = $this->GroupsUser->find('all', array(
							'conditions' => array('GroupsUser.user_id' => $user['User']['id'],
													'GroupsUser.status' => 'approved')));
						if(!empty($user_groups)) {
							foreach ($user_groups as $user_group) {
								$groups_app_conditions = array('GroupApplication.group_id' => $user_group['GroupsUser']['group_id'],
											'GroupApplication.application_id' => $app['Application']['id']);
								if($this->GroupApplication->hasAny($groups_app_conditions)) {
									$groups_app = $this->GroupApplication->find('first', array(
										'conditions' => $groups_app_conditions));
									if($groups_app['GroupApplication']['count'] >= 4) {
										//Send email to group members
										$group_users = $this->GroupsUser->find('all', array(
											'conditions' => array('GroupsUser.group_id' => $groups_app['GroupApplication']['group_id'],
												'GroupsUser.status' => 'approved'),
											'recursive' => 0));
										if(!empty($group_users)) {
											foreach ($group_users as $group_user) {
												$user_app_check_conditions = array('UserApplication.user_id' => $group_user['GroupsUser']['user_id'],
													'UserApplication.application_id' => $app['Application']['id']);
												$email_user = $this->User->find('first', array('conditions' => array('User.id' => $group_user['GroupsUser']['user_id']),
													'recursive' => 0));
												if($this->UserApplication->hasAny($user_app_check_conditions)) {
													//Do nothing since user already tried the app
												} else {
													//Send email - since user doesn't have the applicaiton
													$Email = new CakeEmail();
													$Email->emailFormat('html');
													$Email->template('default');
													//$Email->format('html');
													$Email->from(array('NO-REPLY@androidgeek.vlab.asu.edu' => 'Secure Social App Store'));
													$Email->to($email_user['User']['email']);
													$Email->subject('4 Members of your group has just downloaded a new App!!!');

													$message = "<div class=\"container\">Hello ".$email_user['User']['first_name']." ".
																$email_user['User']['last_name']."<br>".
																"<div style=\"margin:10px\">".										
																"<div style=\"margin:5px\"><b>".$app['Application']['title']."</b><br>".
																"<p>".$app['Application']['description']."</p>".
																"</div></div></div>";
													
													$Email->send($message);

													//Send push notifications
													$HttpSocket = new HttpSocket();
													$options = array(
													  'header' => array(
													    'Content-Type' => 'application/json',
													    'Authorization' => 'key=AIzaSyBEU26a09YUPirxWI0JgfMMM30Ubr8uwms'
													  )
													);    
													$data = array("time_to_live" => 108,
															  		"data"=>array(
																		"Message" => "4 members of your group has just downloaded a new App!!!",
																		"type" => 'application',
																		'id' => $app['Application']['id']
																	),
															  "registration_ids" => array($friend_detail['User']['registration_id'])
															);
													$HttpSocket->post('https://android.googleapis.com/gcm/send', $data, $options);
												}
											}
										}
										$groups_app['GroupApplication']['count'] = 0;
										$this->GroupApplication->save($groups_app);
									} else {
										$groups_app['GroupApplication']['count'] = $groups_app['GroupApplication']['count']+1;
										$this->GroupApplication->save($groups_app);
									}
								} else {
									//Add the group and application to track the count to 1
								}
							}
						}
						return $this->response;
					} else {
						$result = "Invalid Token";
						$this->set('_serialize',array($result));
					}
				// }
			} else {
				//Check if the user is logged in 
				if($this->Session->check('User.id')){
				//If there is a session set
					$app = $this->Application->find('first', array('conditions'=>array('Application.id' => $id)));
					$name = $this->ApplicationRevision->find('first', 
											array('conditions' => array('ApplicationRevision.app_id' => $id),
													'order' => array('ApplicationRevision.id' => 'desc')));
					//Send an email
					//$content = "Testing CakeEmail layout and template";
					//$this->set('content', $content);
					$Email = new CakeEmail();
					$Email->emailFormat('html');
					$Email->template('default');
					//$Email->format('html');
					$Email->from(array('NO-REPLY@androidgeek.vlab.asu.edu' => 'Secure Social App Store'));
					$Email->to('avinash.vllbh@gmail.com');
					$Email->subject('About');
					$Email->send("Testing CakeEmail layout and template");

					//Send push notification
					$HttpSocket = new HttpSocket();
					$options = array(
						'header' => array(
								'Content-Type' => 'application/json',
								'Authorization' => 'key=AIzaSyBEU26a09YUPirxWI0JgfMMM30Ubr8uwms'
						)
					);
														$data = array("time_to_live" => 108,
											  		"data"=>array(
														"Notification" => array(
															"id" => "3",
															"user_id" => "15",
															"description" => "Avinash Vallabhaneni request to join json",
															"status" => false
														)
													),
											  "registration_ids" => array("APA91bEAd3gIVxv0eA_EvH0ueFsBgMZQL136yDPSdCTCOQlI8YZwDzRJiLdG-Wloc0zQOuQNhaFboJfrlIj0lo8y76-XEBBdMohma1Y6ViirqTljkKJ8vMHplsySkzesF05aUoo7qrl1-6mZy-q2MW7Yl7HdHPCHWw")
											);
					$HttpSocket->post('https://android.googleapis.com/gcm/send', json_encode($data), $options);
					$this->response->file($app['Application']['path'], array('download' => true, 'name' => $name['ApplicationRevision']['filename']));
					return $this->response;
				} else {
					$this->Session->setFlash("You need to login to download the applicaiton");
					$this->redirect($this->Auth->redirectUrl(array('controller' => 'users' , 'action' => 'login' )));
				}
			}
		}
	}
}






