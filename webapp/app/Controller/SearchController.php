<?php

App::uses('AppController', 'Controller');

/**
 * Applications Controller
 *
 * @property Application $Application
 * @property PaginatorComponent $Paginator
 * @property SessionComponent $Session
 */

class SearchController extends AppController {

/**
 * Components
 *
 * @var array
 */
	public $components = array('Paginator', 'Session');
	public $uses = array('User','Application','Group');


	public function beforeFilter() {
		parent::beforeFilter();
		//$this->Auth->allow('add');
		$this->Auth->allow('search_users','search_applications','search_groups');
	}

	public function search_users() {
		if(isset($this->request->params['pass'][0])) {
			/*
			* Validate the search data using filter functions
			*/
			// $conditions = array();
			$search_query = $this->request->params['pass'][0];
			$search = addslashes($search_query);

			//$conditions = array('OR' => array('User.first_name like' => "%".$search."%",
												//'User.last_name like' => "%".$search."%"));
			$conditions = array();
			array_push($conditions, array('OR' => array('User.first_name like' => "%".$search."%",
											'User.last_name like' => "%".$search."%")));
			$search_query = $this->request->params['pass'][0];
			$queries = explode(" ", $search_query);
			if(count($queries) > 1) {
				foreach (explode(" ", $search_query) as $search) {
				 	$search = addslashes($search);
				 	array_push($conditions, array('OR' => array(
			          	'User.first_name like' => "%".$search."%",
			          	'User.last_name like' => "%".$search."%"
			 		)));
				}
			}
			$result = array();
			foreach ($conditions as $condition) {
				$search_results = $this->User->find('all', array(
					'conditions' => $condition,
					'recursive' => 0));
				if(!empty($search_results)) {
					foreach ($search_results as $search_result) {
						array_push($result, $search_result);
					}
				}
			}
			$this->set('result', $result);
			$this->set('_serialize', array('result'));
		} else {
			$this->redirect(array('controller' => 'users', 'action' => 'index.json'));
		}
	}

	public function search_applications() {
		if(isset($this->request->params['pass'][0])) {
			$search_query = $this->request->params['pass'][0];
			$search = addslashes($search_query);

			//$conditions = array('OR' => array('User.first_name like' => "%".$search."%",
												//'User.last_name like' => "%".$search."%"));
			$conditions = array();
			array_push($conditions, array('OR' => array('Application.title like' => "%".$search."%",
											'Application.description like' => "%".$search."%")));
			$search_query = $this->request->params['pass'][0];
			$queries = explode(" ", $search_query);
			if(count($queries) > 1) {
				foreach (explode(" ", $search_query) as $search) {
				 	$search = addslashes($search);
				 	array_push($conditions, array('OR' => array(
			          	'Application.title like' => "%".$search."%",
						'Application.description like' => "%".$search."%"
			 		)));
				}
			}
			$result = array();
			foreach ($conditions as $condition) {
				$search_results = $this->Application->find('all', array(
					'conditions' => $condition,
					'recursive' => -1));
				if(!empty($search_results)) {
					foreach ($search_results as $search_result) {
						array_push($result, $search_result);
					}
					
				}
			}
			$this->set('result', $result);
			$this->set('_serialize', array('result'));
		} else {
			$this->redirect(array('controller' => 'applications', 'action' => 'index.json'));
		}
	}

	public function search_groups() {
		if(isset($this->request->params['pass'][0])) {
			$search_query = $this->request->params['pass'][0];
			$search = addslashes($search_query);

			//$conditions = array('OR' => array('User.first_name like' => "%".$search."%",
												//'User.last_name like' => "%".$search."%"));
			$conditions = array();
			array_push($conditions, array('OR' => array('Group.name like' => "%".$search."%",
											'Group.description like' => "%".$search."%")));
			$search_query = $this->request->params['pass'][0];
			$queries = explode(" ", $search_query);
			if(count($queries) > 1) {
				foreach (explode(" ", $search_query) as $search) {
				 	$search = addslashes($search);
				 	array_push($conditions, array('OR' => array(
			          	'Group.name like' => "%".$search."%",
						'Group.description like' => "%".$search."%"
			 		)));
				}
			}
			$result = array();
			foreach ($conditions as $condition) {
				$search_results = $this->Group->find('all', array(
					'conditions' => $condition,
					'recursive' => -1));
				if(!empty($search_results)) {
					foreach ($search_results as $search_result) {
						array_push($result, $search_result);
					}
				}
			}
			$this->set('result', $result);
			$this->set('_serialize', array('result'));
		} else {
			$this->redirect(array('controller' => 'groups', 'action' => 'index.json'));
		}
	}
}