<?php
App::uses('AppController', 'Controller');
/**
 * UserFollowers Controller
 *
 * @property UserFollower $UserFollower
 * @property PaginatorComponent $Paginator
 * @property SessionComponent $Session
 */
class UserFollowersController extends AppController {

/**
 * Components
 *
 * @var array
 */
	public $components = array('Paginator', 'Session');

/**
 * index method
 *
 * @return void
 */
	public function index() {
		$this->UserFollower->recursive = 0;
		$this->set('userFollowers', $this->Paginator->paginate());
	}

/**
 * view method
 *
 * @throws NotFoundException
 * @param string $id
 * @return void
 */
	public function view($id = null) {
		if (!$this->UserFollower->exists($id)) {
			throw new NotFoundException(__('Invalid user follower'));
		}
		$options = array('conditions' => array('UserFollower.' . $this->UserFollower->primaryKey => $id));
		$this->set('userFollower', $this->UserFollower->find('first', $options));
	}

/**
 * add method
 *
 * @return void
 */
	public function add() {
		if ($this->request->is('post')) {
			$this->UserFollower->create();
			if ($this->UserFollower->save($this->request->data)) {
				$this->Session->setFlash(__('The user follower has been saved.'));
				return $this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The user follower could not be saved. Please, try again.'));
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
		if (!$this->UserFollower->exists($id)) {
			throw new NotFoundException(__('Invalid user follower'));
		}
		if ($this->request->is(array('post', 'put'))) {
			if ($this->UserFollower->save($this->request->data)) {
				$this->Session->setFlash(__('The user follower has been saved.'));
				return $this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The user follower could not be saved. Please, try again.'));
			}
		} else {
			$options = array('conditions' => array('UserFollower.' . $this->UserFollower->primaryKey => $id));
			$this->request->data = $this->UserFollower->find('first', $options);
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
		$this->UserFollower->id = $id;
		if (!$this->UserFollower->exists()) {
			throw new NotFoundException(__('Invalid user follower'));
		}
		$this->request->onlyAllow('post', 'delete');
		if ($this->UserFollower->delete()) {
			$this->Session->setFlash(__('The user follower has been deleted.'));
		} else {
			$this->Session->setFlash(__('The user follower could not be deleted. Please, try again.'));
		}
		return $this->redirect(array('action' => 'index'));
	}}
