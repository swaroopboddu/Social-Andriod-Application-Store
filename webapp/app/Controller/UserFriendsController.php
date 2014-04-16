<?php
App::uses('AppController', 'Controller');
/**
 * UserFriends Controller
 *
 * @property UserFriend $UserFriend
 * @property PaginatorComponent $Paginator
 * @property SessionComponent $Session
 */
class UserFriendsController extends AppController {

/**
 * Components
 *
 * @var array
 */
	public $components = array('Paginator', 'Session');
	public $uses = array('UserFriend','User','Notification');

	public function beforeFilter() {
		parent::beforeFilter();
		//$this->Auth->allow('add');
		$this->Auth->allow('add_friend','unfriend','confirm_friend');
	}

/**
 * index method
 *
 * @return void
 */
	public function index() {
		$headers = apache_request_headers();
		if(isset($headers['token'])) {
			$token = $headers['token'];
			$user = $this->User->find('first', array(
				'conditions' => array('User.token' => $token),
				'fields' => array('User.id'),
				'recursive' => 0));
			//Check if the token's --> UserID is valid
			if(isset($user['User']['id'])) {
				if(isset($this->request->params['pass'][0])) {
					$req_user = $this->User->find('first', array(
						'conditions' => array('User.id' => $this->request->params['pass'][0])));
					if(isset($req_user['User']['id'])) {
						$result = $this->UserFriend->find('all',array(
							'conditions' => array('UserFriend.user_id' => $req_user['User']['id'],
								'UserFriend.status' => 0)));
					} else {
						$result = "Invalid UserID requested";
					}

				} else {
					$result = $this->UserFriend->find('all', array(
						'conditions' => array('UserFriend.user_id' => $user['User']['id'],
							'UserFriend.status' => 0)));
				}
			} else {
				$result = "Failure - Invalid Token ID";
			}	
		} else {
			$result = "Failure - Token ID required";
		}
		$this->set('result', $result);
		$this->set('_serialize', array('result'));		
		//
		// $this->UserFriend->recursive = 0;
		// $this->set('userFriends', $this->Paginator->paginate());
	}

/**
 * view method
 *
 * @throws NotFoundException
 * @param string $id
 * @return void
 */
	public function view($id = null) {
		if (!$this->UserFriend->exists($id)) {
			throw new NotFoundException(__('Invalid user friend'));
		}
		$options = array('conditions' => array('UserFriend.' . $this->UserFriend->primaryKey => $id));
		$this->set('userFriend', $this->UserFriend->find('first', $options));
	}

/**
 * add method
 *
 * @return void
 */
	public function add() {
		if ($this->request->is('post')) {
			$this->UserFriend->create();
			if ($this->UserFriend->save($this->request->data)) {
				$this->Session->setFlash(__('The user friend has been saved.'));
				return $this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The user friend could not be saved. Please, try again.'));
			}
		}
	}

	public function add_friend($id = null) {
		if ($this->request->is('post')) {
			$headers = apache_request_headers();
			if(isset($headers['token'])){
				$token = $headers['token'];
				$user = $this->User->find('first', array(
						'conditions' => array('User.token' => $token),
						'recursive' => 0));
				//pr($user);
				if(isset($user['User']['id'])) {
					if(isset($this->request->params['pass'][0])){
						$friend_user = $this->User->find('first', array('conditions' => 
								array('User.id' => $this->request->params['pass'][0]),
									'recursive' => 0));
						//Check if the id passed is valid
						if(isset($friend_user['User']['id'])) {
							$friend['UserFriend']['user_id'] = $user['User']['id'];
							$friend['UserFriend']['friend_user_id'] = $friend_user['User']['id'];
							$friend['UserFriend']['status'] = 1;
							$this->UserFriend->create();
							if ($this->UserFriend->save($friend)) {
								//Notify the user that someone sent him a friend request
								$notify['Notification']['user_id'] = $friend_user['User']['id'];
								$notify['Notification']['description'] = $user['User']['first_name']." ".$user['User']['last_name']." sent you a friend request";
								$notify['status'] = 0;
								if($this->Notification->save($notify)){
									$result = "Success";
								} else {
							//$this->Session->setFlash(__('The user friend request could not be saved. Please, try again.'));
									$result = "Failure";
								}
							} else {
								$result = "Failure";
							}
							$friend['UserFriend']['user_id'] = $friend_user['User']['id'];
							$friend['UserFriend']['friend_user_id'] = $user['User']['id'];
							$friend['UserFriend']['status'] = 2;
							$this->UserFriend->create();
							if($this->UserFriend->save($friend)) {
								$result = "Success";
							} else {
								$result = "Failure";
							}
						} else {
							$result = "Failure-Invalid user to add as a friend";
						}
					} else {
						$result = "Failure-User ID required to for friend request";
					}
				} else {
					$result = "Failure-Invalid token - User unauthorized";
				}
			} else {
				$result = "Failure-User TokenID required to authorize";
			}
			$this->set('result', $result);
			$this->set('_serialize', array('result'));
		}
	}

	// To accept or reject the friend request
	public function confirm_friend() {
		if ($this->request->is('post')) {
			$headers = apache_request_headers();
			if(isset($headers['token'])){
				$token = $headers['token'];
				$user = $this->User->find('first', array(
						'conditions' => array('User.token' => $token),
						'recursive' => 0));
				if(isset($user['User']['id'])) {
					$friend = $this->request->data;
					$conditions = array('UserFriend.user_id' => $friend['UserFriend']['id'],
										'UserFriend.friend_user_id' => $user['User']['id']);
					if($this->UserFriend->hasAny($conditions)) {
						if($friend['UserFriend']['status'] == "accept") {
							$record = $this->UserFriend->find('first', array(
								'conditions' => $conditions,
								'recursive' => 0));
							$this->UserFriend->id = $record['UserFriend']['id'];
							if($this->UserFriend->saveField('status', 0)) {
								$conditions = array('UserFriend.user_id' => $record['UserFriend']['friend_user_id'],
									'UserFriend.friend_user_id' => $record['UserFriend']['user_id']);
								$get_id = $this->UserFriend->find('first',array('conditions' => $conditions));
								$this->UserFriend->id = $get_id['UserFriend']['id'];
								if($this->UserFriend->saveField('status', 0)) {
									$result = "Success";
								}
							}
						} else {
							$this->UserFriend->deleteAll($conditions);
							$conditions = array('UserFriend.user_id' => $user['User']['id'],
										'UserFriend.friend_user_id' => $friend['UserFriend']['id']);
							$this->UserFriend->deleteAll($conditions);
							$result = "Success";
						}
					} else {
						$result = "Failure-Invalid Action";
					}
				} else {
					$result = "Failure-Invalid token";
				}
			} else {
				$result = "Failure-Valid token required";
			}
			$this->set('result', $result);
			$this->set('_serialize', array('result'));
		}
	}

	public function unfriend($id = null) {
		if ($this->request->is('post')) {
			$headers = apache_request_headers();
			if(isset($headers['token'])){
				$token = $headers['token'];
				$user = $this->User->find('first', array(
						'conditions' => array('User.token' => $token),
						'recursive' => 0));
				//pr($user);
				if(isset($user['User']['id'])) {
					if(isset($this->request->params['pass'][0])){
						$friend_user = $this->User->find('first', array('conditions' => 
								array('User.id' => $this->request->params['pass'][0]),
									'recursive' => 0));
						//Check if the id passed is valid
						if(isset($friend_user['User']['id'])) {
							//Delete the record 
							if($this->UserFriend->deleteAll(array('UserFriend.user_id' => $user['User']['id'],
																'UserFriend.friend_user_id' => $friend_user['User']['id']))) {
								if($this->UserFriend->deleteAll(array('UserFriend.friend_user_id' => $user['User']['id'],
																'UserFriend.user_id' => $friend_user['User']['id']))) {
									$result = "Success";
								} else {
									$result = "Failure";
								}
							} else {
							//$this->Session->setFlash(__('The user friend request could not be saved. Please, try again.'));
									$result = "Failure";
							}
						} else {
							$result = "Failure-Invalid user to unfriend";
						}
					} else {
						$result = "Failure-User ID required to unfriend";
					}
				} else {
					$result = "Failure-Invalid token - User unauthorized";
				}
			} else {
				$result = "Failure-User TokenID required to authorize";
			}
			$this->set('result', $result);
			$this->set('_serialize', array('result'));
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
		if (!$this->UserFriend->exists($id)) {
			throw new NotFoundException(__('Invalid user friend'));
		}
		if ($this->request->is(array('post', 'put'))) {
			if ($this->UserFriend->save($this->request->data)) {
				$this->Session->setFlash(__('The user friend has been saved.'));
				return $this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The user friend could not be saved. Please, try again.'));
			}
		} else {
			$options = array('conditions' => array('UserFriend.' . $this->UserFriend->primaryKey => $id));
			$this->request->data = $this->UserFriend->find('first', $options);
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
		$this->UserFriend->id = $id;
		if (!$this->UserFriend->exists()) {
			throw new NotFoundException(__('Invalid user friend'));
		}
		$this->request->onlyAllow('post', 'delete');
		if ($this->UserFriend->delete()) {
			$this->Session->setFlash(__('The user friend has been deleted.'));
		} else {
			$this->Session->setFlash(__('The user friend could not be deleted. Please, try again.'));
		}
		return $this->redirect(array('action' => 'index'));
	}}
