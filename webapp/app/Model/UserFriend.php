<?php
App::uses('AppModel', 'Model');
/**
 * UserFriend Model
 *
 */
class UserFriend extends AppModel {

/**
 * Use table
 *
 * @var mixed False or table name
 */
	public $useTable = 'user_friend';

/**
 * Display field
 *
 * @var string
 */
	public $displayField = 'id';

	public $belongsTo = array(
		'User' => array(
			'className' => 'User',
			'foreignKey' => 'friend_user_id',
			'fields' => array('User.id', 'User.first_name', 'User.last_name', 'User.email', 'User.phone', 'User.role')
			));

}
