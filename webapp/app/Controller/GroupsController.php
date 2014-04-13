<?php
App::uses('AppController', 'Controller');
/**
 * Groups Controller
 *
 * @property Group $Group
 * @property PaginatorComponent $Paginator
 * @property SessionComponent $Session
 */
class GroupsController extends AppController {

/**
 * Components
 *
 * @var array
 */
	public $components = array('Paginator', 'Session');
	public $uses = array('Group','User','GroupsUser');

	public function beforeFilter() {
		parent::beforeFilter();
		//$this->Auth->allow('add');
		$this->Auth->allow('add');
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
				$groups = $this->GroupsUser->find('all', array(
					'conditions' => array('GroupsUser.user_id' => $user['User']['id'])));
				pr($groups);
				$this->set('groups', $groups);
				$this->set('_serialize', array('groups'));
		} else {
			//$this->Group->recursive = -1;
			//$group = $this->Group->find('all', array('order' => array('Group.id' => 'desc')));
 			$this->set('groups', $this->Paginator->paginate());
 			//$this->set('groups', $group);
			$this->set('_serialize', array('groups'));
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
		if (!$this->Group->exists($id)) {
			throw new NotFoundException(__('Invalid group'));
		}
		$options = array('conditions' => array('Group.' . $this->Group->primaryKey => $id));
		$this->set('group', $this->Group->find('first', $options));
	}

/**
 * add method
 *
 * @return void
 */
	public function add() {
		if ($this->request->is('post')) {
			$headers = apache_request_headers();
			if(isset($headers['token'])) {
				$token = $headers['token'];
				$user = $this->User->find('first', array(
					'conditions' => array('User.token' => $token),
					'recursive' => 0));
				if(isset($user['User'])) {
					pr($user);
					$this->request->data['Group']['user_id'] = $user['User']['id'];
					$this->request->data['Group']['created_on'] = date('Y-m-d H:i:s');
					pr($this->request->data);
					$this->Group->create();
					if ($this->Group->save($this->request->data)) {
					 	$lastInsert = $this->Group->getLastInsertID();
					 	//To save the user also as one of the mems of group
					 	$group_user = array();
					 	$group_user['group_id'] = $lastInsert;
					 	$group_user['user_id'] = $user['User']['id'];
					 	$group_user['status'] = "pending";
					 	$group_user['added_on'] = date('Y-m-d H:i:s');
					 	$this->Group->create();
					 	$this->GroupsUser->save($group_user);

					 	//Result containing only the created group data
					 	
					 	$result = $this->Group->find('first', array(
					 		'conditions' => array('Group.id' => $lastInsert)));
					 	$this->set('result', $result);
						$this->set('_serialize',array('result'));
			 		} else {
			 			$result = "Unable to create group";
			 			$this->set('result', $result);
						$this->set('_serialize',array('result'));
			 		}
				} else {
					$result = "Invalid token - No such user";
					return $this->redirect(array('action' => 'index.json'));
				}
			} else {
				$result = "Token ID required";
				return $this->redirect(array('action' => 'index.json'));
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
		if (!$this->Group->exists($id)) {
			throw new NotFoundException(__('Invalid group'));
		}
		if ($this->request->is(array('post', 'put'))) {
			$headers = apache_request_headers();
			if(isset($headers['token'])) {
				$user = $this->User->find('first', array(
					'conditions' => array('User.token' => $token),
					'recursive' => 0));
				$group = $this->Group->find('first', array('conditions' => array('Group.id' => $id)));
				//To check if admin is editing the Group info
				if($user['User']['id'] == $group['Group']['user_id']) { 
					if ($this->Group->save($this->request->data)) {
						$result = $this->request->data;
					 	$this->set('result', $result);
						$this->set('_serialize',array('result'));
			 		} else {
						$result = "Failure";
					 	$this->set('result', $result);
						$this->set('_serialize',array('result'));
					} 
			//else {
			//	$this->Session->setFlash(__('The group could not be saved. Please, try again.'));
				} else {
					$result = "Did you really think you can do that!!!";
					$this->set('result', $result);
					$this->set('_serialize',array('result'));
				}
			}
			//$options = array('conditions' => array('Group.' . $this->Group->primaryKey => $id));
			//$this->request->data = $this->Group->find('first', $options);
			return $this->redirect(array('action' => 'index.json'));
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
		$this->Group->id = $id;
		if (!$this->Group->exists()) {
			throw new NotFoundException(__('Invalid group'));
		}
		$this->request->onlyAllow('post', 'delete');
		$headers = apache_request_headers();
		if(isset($headers['token'])) {
			$user = $this->User->find('first', array(
				'conditions' => array('User.token' => $token),
				'recursive' => 0));
			$group = $this->Group->find('first', array('conditions' => array('Group.id' => $id)));
			//To check if admin is editing the Group info
			if($user['User']['id'] == $group['Group']['user_id']) { 
				if ($this->Group->delete()) {
					$result = "Success";
					$this->set('result', $result);
					$this->set('_serialize',array('result'));
			//$this->Session->setFlash(__('The group has been deleted.'));
				} else {
					$result = "Failed to Delete the group";
					$this->set('result', $result);
					$this->set('_serialize',array('result'));
				}
			} else {
				$result = "Did you really think you can do that!!!";
				$this->set('result', $result);
				$this->set('_serialize',array('result'));
			}
			//$this->Session->setFlash(__('The group could not be deleted. Please, try again.'));
		}
		return $this->redirect(array('action' => 'index.json'));
	}}
