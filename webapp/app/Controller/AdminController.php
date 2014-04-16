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
class AdminController extends AppController {

/**
 * Components
 *
 * @var array
 */
	public $components = array('Paginator', 'Session');
	public $uses = array('User','Application','ApplicationRevision','Group');

/**
 * index method
 *
 * @return void
 */
	public function index() {
		$dev_users = $this->User->find('count', array(
			'conditions' => array('User.role' => 'developer')));
		$mobile_users = $this->User->find('count', array(
			'conditions' => array('User.role' => 'mobile')));
		$applications = $this->Application->find('count');
		$groups = $this->Group->find('count');
		$this->set('dev_users', $dev_users);
		$this->set('mobile_users', $mobile_users);
		$this->set('applications', $applications);
		$this->set('groups', $groups);
	}

	public function users() {
		$users = $this->User->find('all',array('order' => array('User.id DESC')));

		$this->set('users',$users);
	}

	public function applications() {
		$applications = $this->Application->find('all',array('order' => array('User.id DESC')));

		$this->set('applications',$applications);
	}

	public function groups() {
		$groups = $this->Group->find('all',array('order' => array('User.id DESC')));

		$this->set('groups',$groups);
	}

	public function update() {
		if ($this->request->is(array('post', 'put'))) {
			$email = $this->request->data;
			if($email['Application']['Users'] == 0) {
				$users = $this->User->find('all', array('conditions' => array('User.role' => 'developer')));
				if(!empty($email['Application']['Message']) && !empty($users))
				foreach ($users as $user) {
					$Email = new CakeEmail();
					$Email->emailFormat('html');
					$Email->template('default');
					//$Email->format('html');
					$Email->from(array('NO-REPLY@androidgeek.vlab.asu.edu' => 'Secure Social App Store'));
					$Email->to($User['User']['email']);
					//$Email->to('avinash.vllbh@gmail.com');
					$Email->subject('System Update to all the Developers');
					$Email->send($email['Application']['Message']);
				}
			} elseif($email['Application']['Users'] == 1) {
				$users = $this->User->find('all', array('conditions' => array('User.role' => 'mobile')));
				if(!empty($email['Application']['Message']) && !empty($users))
				foreach ($users as $user) {
					$Email = new CakeEmail();
					$Email->emailFormat('html');
					$Email->template('default');
					//$Email->format('html');
					$Email->from(array('NO-REPLY@androidgeek.vlab.asu.edu' => 'Secure Social App Store'));
					$Email->to($User['User']['email']);
					$Email->subject('System update to all the users');
					$Email->send($email['Application']['Message']);
				}

			} else {
				$users = $this->User->find('all');
				if(!empty($email['Application']['Message']) && !empty($users))
				foreach ($users as $user) {
					$Email = new CakeEmail();
					$Email->emailFormat('html');
					$Email->template('default');
					//$Email->format('html');
					$Email->from(array('NO-REPLY@androidgeek.vlab.asu.edu' => 'Secure Social App Store'));
					$Email->to($User['User']['email']);
					$Email->subject('System Update - Secure Social');
					$Email->send($email['Application']['Message']);
				}

			}
			$this->Session->setFlash("Email notification has been sent");
		}
		$options = array("Developers","mobile users", "all users");
		$this->set('options', $options);
	}

}