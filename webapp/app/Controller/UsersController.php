<?php
App::uses('AppController', 'Controller');
/**
 * Users Controller
 *
 * @property User $User
 * @property PaginatorComponent $Paginator
 * @property SessionComponent $Session
 */
class UsersController extends AppController {

/**
 * Components
 *
 * @var array
 */
	public $components = array('Paginator', 'Session');
	public $uses = array('User','UserFriend', 'UserFollower');

public function beforeFilter() {
	parent::beforeFilter();
	//$this->Auth->allow('index');
	$this->Auth->allow('mobile_users');
	$this->Auth->allow('developers');
	$this->Auth->allow('add');
	$this->Auth->allow('mobile_login');
	if($this->Session->check('User.id')){
		$this->Auth->allow('logout');
	}
}

public function isAuthorized($user)	{
	if(in_array($this->action, array('edit','delete'))) {
		if($user['id'] != $this->request->params['pass'][0]) {
			return false;
		}
		return true;
	}
}
public function login() {
	if($this->request->is('post')) {
		$date = date('Y/m/d H:i:s');
		if($this->Auth->login()) {
				$user = $this->Auth->user();
			//To setup the session once login is successful
				$this->Session->write('User.first_name', $user['first_name']);
				$this->Session->write('User.last_name',  $user['last_name']);
				$this->Session->write('User.id',         $user['id']);
				$this->Session->write('User.email',      $user['email']);
				$this->Session->write('User.role',      $user['role']);
				//Update logged in time to users database
				$this->User->updateAll(array('User.last_login'=>"'".$date."'"),array('User.id' => $this->Session->read('User.id')));
				$this->Session->setFlash('Welcome '.$user['first_name']." ".$user['last_name']);
			$this->redirect($this->Auth->redirectUrl(array('controller' => 'users' , 'action' => 'index' )));
		} else {
			$this->Session->setFlash('Your email/password combination was incorrect');
		}
	}
}

/***
// To check user access from mobile
// Generates a UUID to the user and sends it as a response to REST 
***/
public function mobile_login() {
	if($this->request->is('post')) {
		$date = date('Y/m/d H:i:s');
		if($this->Auth->login()) {
				$user = $this->Auth->user();
				$user_uuid = String::uuid();
				$this->User->updateAll(array(
					'User.last_login'=>"'".$date."'",
					'User.token' => "'".$user_uuid."'"),array('User.id' => $user['id']));
				$result = $this->User->find('first', array(
					'conditions' => array('User.id' => $user['id']),
					'fields' => array('User.id', 'User.first_name', 'User.last_name', 'User.email',
					 'User.phone', 'User.role', 'User.token')));
				$this->set('result',$result);
				$this->set('_serialize',array('result'));		
		} else {
			$result = "Failure";
			$this->set('_serialize', array($result));
			$this->response->statusCode(401);
		}
	}
}

public function logout() {
	if($this->Session->check('User.id')) {
		$date = date('Y/m/d H:i:s');
		if($this->Auth->logout()) {
			//To setup the session once logout is successful
			$this->Session->delete('User.id');
			$this->Session->delete('User.first_name');
			$this->Session->delete('User.last_name');
			$this->Session->delete('User.email');
			$this->Session->destroy();
			$this->redirect($this->Auth->redirectUrl());
		} 
	}
}

/**
 * index method
 *
 * @return void
 */
	public function index() {
		// if($this->request->params['pass'] != null)
		// {
		// 	pr($this->request->params);
		// 	exit(1);
		// }
		$headers = apache_request_headers();

		if($this->Session->read('User.id') != null || isset($headers['token'])) {
			if($this->Session->read('User.id') != null) {
				$id = $this->Session->read('User.id');
				if(!empty($id)) {
					$result = $this->User->find('first', array('conditions'=>array('User.id' => $id)));
					$this->set('result', $result);
					$this->set('_serialize', array('result'));
				}
			}
			if(isset($headers['token'])) {
			//Return token --> users list of applications
				$token = $headers['token'];
				$user = $this->User->find('first', array(
					'conditions' => array('User.token' => $token),
					'recursive' => 0));
				if(isset($user['User']['id'])) {
					//Check if params is set -- List of apps for passed UserID
					if(isset($this->request->params['pass'][0])) {
						$conditions = array('User.id' => $this->request->params['pass'][0]);
						if($this->User->hasAny($conditions)) {
							$result = $this->User->find('first', array(
										'conditions' => array('User.id' => $this->request->params['pass'][0]),
										'recursive' => 1));
							if($result['User']['role'] == 'mobile') {
								$friend_check = $this->UserFriend->find('first',array(
									'conditions' => array('UserFriend.user_id' => $user['User']['id'],
										'UserFriend.friend_user_id' => $result['User']['id'])));
								if(!empty($friend_check)) {

									if($friend_check['UserFriend']['status'] == 0) {
										$result['User']['relationship'] = "Friends";
									} elseif($friend_check['UserFriend']['status'] == 1) {
										$result['User']['relationship'] = "Request Sent";
									} else {
										$result['User']['relationship'] = "Request Pending";
									}
								} else {
									$result['User']['relationship'] = "Not Friends";
								}
							} else {
								$follower_check = $this->UserFollower->find('first', array(
									'conditions' => array('UserFollower.user_id' => $result['User']['id'],
										'UserFollower.follower_user_id' => $user['User']['id'])));
								if(!empty($follower_check)) {
									$result['User']['relationship'] = "Following";
								} else {
									$result['User']['relationship'] = "Not Following";
								}
							}
							//pr($result);
							$this->set('result',$result);
							$this->set('_serialize', array('result'));
						} else {
							$result = "Invalid User ID";
							$this->set('result',$result);
							$this->set('_serialize', array('result'));
						}
					} else { 
						$result = $this->User->find('all', array(
						'conditions' => array('User.id' => $user['User']['id']),
						'recursive' => 2));
						$this->set('result',$result);
						$this->set('_serialize', array('result'));
					}
				} else {
					//Invalid token
					$result = "Invalid token id";
					$this->set('_serialize', array($result));
				}
			}
		} else { 
			$this->redirect(array('controller' => 'users', 'action' => 'login'));
	}
		//$this->set('users', $this->Paginator->paginate());
	}

	public function mobile_users() {
		$headers = apache_request_headers();
		if(isset($headers['token'])) {
			$token = $headers['token'];
			$user = $this->User->find('first', array(
				'conditions' => array('User.token' => $token),
				'fields' => array('User.id'),
				'recursive' => 0));
			//Check if the token's --> UserID is valid
			if(isset($user['User']['id'])) {
				$result = $this->User->find('all', array(
					'conditions' => array('User.role' => 'mobile'),
					'order' => array('User.id DESC'),
					'recursive' => 0,
					'fields' => array('User.id', 'User.first_name', 'User.last_name', 'User.email', 'User.phone', 'User.role')));
			} else {
				$result = "Failure - Invalid Token ID";
			}	
		} else {
			$result = "Failure - Token ID required";
		}
		$this->set('result', $result);
		$this->set('_serialize', array('result'));

	}

	public function developers() {
		$headers = apache_request_headers();
		if(isset($headers['token'])) {
			$token = $headers['token'];
			$user = $this->User->find('first', array(
				'conditions' => array('User.token' => $token),
				'fields' => array('User.id'),
				'recursive' => 0));
			//Check if the token's --> UserID is valid
			if(isset($user['User']['id'])) {
				$result = $this->User->find('all', array(
					'conditions' => array('User.role' => 'developer'),
					'order' => array('User.id DESC'),
					'recursive' => 0,
					'fields' => array('User.id', 'User.first_name', 'User.last_name', 'User.email', 'User.phone', 'User.role')));
			} else {
				$result = "Failure - Invalid Token ID";
			}	
		} else {
			$result = "Failure - Token ID required";
		}
		$this->set('result', $result);
		$this->set('_serialize', array('result'));

	}

/**
 * view method
 *
 * @throws NotFoundException
 * @param string $id
 * @return void
 */
	public function view($id = null) {
		if (!$this->User->exists($id)) {
			throw new NotFoundException(__('Invalid user'));
		}
		$options = array('conditions' => array('User.' . $this->User->primaryKey => $id));
		$this->set(array(
			'user' => $this->User->find('first', $options),
			'_serialize' => $this->User->find('first', $options)
			));
	}

/**
 * add method
 *
 * @return void
 */
	public function add() {
		if ($this->request->is('post')) {
			$user = $this->request->data;
				if(isset($this->request->params['ext'])) {
					if($this->request->params['ext'] == 'json') {
						$user['User']['role'] = 'mobile';
						$user_uuid = String::uuid();
						$user['User']['token'] = $user_uuid;
					}
				}
			$this->User->create();
			if ($this->User->save($user)) {
				$id = $this->User->getLastInsertID();
				if(isset($this->request->params['ext'])){
					if($this->request->params['ext'] == 'json') {
						$result = $this->User->find('first', array(
							'conditions' => array('User.id' => $id),
							'fields' => array('User.id', 'User.first_name', 'User.last_name', 'User.email',
							 'User.phone', 'User.role', 'User.token')));
						$this->set('result',$result);
						$this->set('_serialize',array('result'));
					}
				}
				else {
					$this->Session->setFlash(__('The user has been saved.'));
					return $this->redirect(array('action' => 'index'));
				}
			} else {
				if(isset($this->request->params['ext'])) {
					if($this->request->params['ext'] == 'json') {
						$conditions = array('User.email' => $user['User']['email']);
						if($this->User->hasAny($conditions)) {
							$result = "Failure-Email ID already exists";
						} else {
							$result = "Failure-Registration failed";
						}
						$this->set('result',$result);
						$this->set('_serialize',array('result'));
					} else {
						$this->Session->setFlash(__('The user could not be saved. Please, try again.'));
					}
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
		if (!$this->User->exists($id)) {
			throw new NotFoundException(__('Invalid user'));
		}
		if ($this->request->is(array('post', 'put'))) {
			if ($this->User->save($this->request->data)) {
				$this->Session->setFlash(__('The user has been saved.'));
				return $this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The user could not be saved. Please, try again.'));
			}
		} else {
			$options = array('conditions' => array('User.' . $this->User->primaryKey => $id));
			$this->request->data = $this->User->find('first', $options);
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
		$this->User->id = $id;
		if (!$this->User->exists()) {
			throw new NotFoundException(__('Invalid user'));
		}
		$this->request->onlyAllow('post', 'delete');
		if($this->Session->read('User.role') == 'admin') {
			if ($this->User->delete()) {
				$this->Session->setFlash(__('The user has been deleted.'));
			} else {
				$this->Session->setFlash(__('The user could not be deleted. Please, try again.'));
			}
		}
		return $this->redirect(array('action' => 'index'));
	}}
