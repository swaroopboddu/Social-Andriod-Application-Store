<?php
App::uses('AppController', 'Controller');
/**
 * GroupApplications Controller
 *
 * @property GroupApplication $GroupApplication
 * @property PaginatorComponent $Paginator
 * @property SessionComponent $Session
 */
class GroupApplicationsController extends AppController {

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
		$this->GroupApplication->recursive = 0;
		$this->set('groupApplications', $this->Paginator->paginate());
	}

/**
 * view method
 *
 * @throws NotFoundException
 * @param string $id
 * @return void
 */
	public function view($id = null) {
		if (!$this->GroupApplication->exists($id)) {
			throw new NotFoundException(__('Invalid group application'));
		}
		$options = array('conditions' => array('GroupApplication.' . $this->GroupApplication->primaryKey => $id));
		$this->set('groupApplication', $this->GroupApplication->find('first', $options));
	}

/**
 * add method
 *
 * @return void
 */
	public function add() {
		if ($this->request->is('post')) {
			$this->GroupApplication->create();
			if ($this->GroupApplication->save($this->request->data)) {
				$this->Session->setFlash(__('The group application has been saved.'));
				return $this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The group application could not be saved. Please, try again.'));
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
		if (!$this->GroupApplication->exists($id)) {
			throw new NotFoundException(__('Invalid group application'));
		}
		if ($this->request->is(array('post', 'put'))) {
			if ($this->GroupApplication->save($this->request->data)) {
				$this->Session->setFlash(__('The group application has been saved.'));
				return $this->redirect(array('action' => 'index'));
			} else {
				$this->Session->setFlash(__('The group application could not be saved. Please, try again.'));
			}
		} else {
			$options = array('conditions' => array('GroupApplication.' . $this->GroupApplication->primaryKey => $id));
			$this->request->data = $this->GroupApplication->find('first', $options);
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
		$this->GroupApplication->id = $id;
		if (!$this->GroupApplication->exists()) {
			throw new NotFoundException(__('Invalid group application'));
		}
		$this->request->onlyAllow('post', 'delete');
		if ($this->GroupApplication->delete()) {
			$this->Session->setFlash(__('The group application has been deleted.'));
		} else {
			$this->Session->setFlash(__('The group application could not be deleted. Please, try again.'));
		}
		return $this->redirect(array('action' => 'index'));
	}}
