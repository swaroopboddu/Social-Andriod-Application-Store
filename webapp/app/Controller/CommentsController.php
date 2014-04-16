<?php
App::uses('AppController', 'Controller');
/**
 * Comments Controller
 *
 * @property Comment $Comment
 * @property PaginatorComponent $Paginator
 * @property SessionComponent $Session
 */
class CommentsController extends AppController {

/**
 * Components
 *
 * @var array
 */
	public $components = array('Paginator', 'Session');
	public $uses = array('Comment','User','Application','UserApplication');

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
		$this->Comment->recursive = 0;
		$this->set('comments', $this->Paginator->paginate());
	}

/**
 * view method
 *
 * @throws NotFoundException
 * @param string $id
 * @return void
 */
	public function view($id = null) {
		if (!$this->Comment->exists($id)) {
			throw new NotFoundException(__('Invalid comment'));
		}
		$options = array('conditions' => array('Comment.' . $this->Comment->primaryKey => $id));
		$this->set('comment', $this->Comment->find('first', $options));
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
			//Return token --> users list of applications
				$token = $headers['token'];
				$user = $this->User->find('first', array(
					'conditions' => array('User.token' => $token),
					'fields' => array('User.id'),
					'recursive' => 0));
				if(isset($user['User']['id'])) {
					$comment = array();
					$comment['Comment']['user_id'] = $user['User']['id'];
					$comment['Comment']['application_id'] = $this->request->data['Comment']['application_id'];
					$comment['Comment']['description'] = $this->request->data['Comment']['description'];
					$comment['Comment']['posted_date'] = date('Y-m-d H:i:s');
					$this->Comment->create();
					if ($this->Comment->save($comment)) {
						$rating = $this->request->data['Comment']['rating'];
						$conditions = array('UserApplication.user_id' => $comment['Comment']['user_id'],
							'UserApplication.application_id' => $comment['Comment']['application_id']);
						$user_app = $this->UserApplication->find('first', array('conditions' => $conditions));
						$this->UserApplication->id = $user_app['UserApplication']['id'];
						$this->UserApplication->saveField('rating', $rating);
						$app = $this->Application->find('first', array(
							'conditions' => array('Application.id' => $this->request->data['Comment']['application_id'])));
						$app['Application']['count_rating'] = $app['Application']['count_rating']+1;
						$app['Application']['rating'] = ($app['Application']['rating']+$rating)/$app['Application']['count_rating'];
						$app['Application']['rating'] = round($app['Application']['rating'],1);
						$this->Application->save($app);
						$result = "Success";
					} else {
						$result = "Failure-unable to save comment";
					}
				} else {
					$result = "Failure - Invalid User ID";
				}
				$this->set('result',$result);
				$this->Set('_serialize', array('result'));
			} else {
				if($this->Session->read('User.id') != null) {
					$user = $this->User->find('first', array(
						'conditions' => array('User.id' => $this->Session->read('User.id')),
						'recursive' => 0));
					if(isset($user['User']['id'])) {
						if($this->Application->hasAny(array(
							'Application.id' => $this->request->data['Comment']['application_id']))) {
							$comment['Comment']['user_id'] = $user['User']['id'];
							$comment['Comment']['application_id'] = $this->request->data['Comment']['application_id'];
							$comment['Comment']['description'] = $this->request->data['Comment']['description'];
							$comment['Comment']['posted_date'] = date('Y-m-d H:i:s');
							$this->Comment->create();
							if ($this->Comment->save($comment)) {
								$this->Session->setFlash(__('The comment has been saved.'));
								return $this->redirect(array(
									'controller' => 'applications', 'action' => 'view', $this->request->data['Comment']['application_id']));
							} else {
								$this->Session->setFlash(__('The comment could not be saved. Please, try again.'));
							}
						} else {
							$this->Session->setFlash(__('The comment could not be saved. Invalid Application.'));
							return $this->redirect(array(
									'controller' => 'applications', 'action' => 'index'));
						}
					} else {
						$this->Session->setFlash(__('Invalid user, please login and try again.'));
						return $this->redirect(array(
							'controller' => 'applications', 'action' => 'index',$this->request->data['Comment']['application_id']));
					}
				} else {
					$this->Session->setFlash(__('Invalid user, please login and try again.'));
					return $this->redirect(array(
						'controller' => 'applications', 'action' => 'view',$this->request->data['Comment']['application_id']));
				}
			}
		}
	}
}

// /**
//  * edit method
//  *
//  * @throws NotFoundException
//  * @param string $id
//  * @return void
//  */
// 	public function edit($id = null) {
// 		if (!$this->Comment->exists($id)) {
// 			throw new NotFoundException(__('Invalid comment'));
// 		}
// 		if ($this->request->is(array('post', 'put'))) {
// 			if ($this->Comment->save($this->request->data)) {
// 				$this->Session->setFlash(__('The comment has been saved.'));
// 				return $this->redirect(array('action' => 'index'));
// 			} else {
// 				$this->Session->setFlash(__('The comment could not be saved. Please, try again.'));
// 			}
// 		} else {
// 			$options = array('conditions' => array('Comment.' . $this->Comment->primaryKey => $id));
// 			$this->request->data = $this->Comment->find('first', $options);
// 		}
// 	}

// /**
//  * delete method
//  *
//  * @throws NotFoundException
//  * @param string $id
//  * @return void
//  */
// 	public function delete($id = null) {
// 		$this->Comment->id = $id;
// 		if (!$this->Comment->exists()) {
// 			throw new NotFoundException(__('Invalid comment'));
// 		}
// 		$this->request->onlyAllow('post', 'delete');
// 		if ($this->Comment->delete()) {
// 			$this->Session->setFlash(__('The comment has been deleted.'));
// 		} else {
// 			$this->Session->setFlash(__('The comment could not be deleted. Please, try again.'));
// 		}
// 		return $this->redirect(array('action' => 'index'));
// 	}}
