<?php
/**
 * Routes configuration
 *
 * In this file, you set up routes to your controllers and their actions.
 * Routes are very important mechanism that allows you to freely connect
 * different URLs to chosen controllers and their actions (functions).
 *
 * CakePHP(tm) : Rapid Development Framework (http://cakephp.org)
 * Copyright (c) Cake Software Foundation, Inc. (http://cakefoundation.org)
 *
 * Licensed under The MIT License
 * For full copyright and license information, please see the LICENSE.txt
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright (c) Cake Software Foundation, Inc. (http://cakefoundation.org)
 * @link          http://cakephp.org CakePHP(tm) Project
 * @package       app.Config
 * @since         CakePHP(tm) v 0.2.9
 * @license       http://www.opensource.org/licenses/mit-license.php MIT License
 */

/* To support REST services */

Router::resourceMap(array(
    array('action' => 'index', 'method' => 'GET', 'id' => false),
    array('action' => 'view', 'method' => 'GET', 'id' => true),
    array('action' => 'add', 'method' => 'POST', 'id' => false),
    array('action' => 'edit', 'method' => 'PUT', 'id' => true),
    array('action' => 'delete', 'method' => 'DELETE', 'id' => true),
    array('action' => 'update', 'method' => 'POST', 'id' => true),
    array('action' => 'mobile_login', 'method' => 'POST', 'id' => false),
    array('action' => 'group_join', 'method' => 'POST', 'id' => true),
    array('action' => 'group_unjoin', 'method' => 'POST', 'id' => true),
    array('action' => 'follow', 'method' => 'POST', 'id' => true),
    array('action' => 'unfollow', 'method' => 'POST', 'id' => true),
    array('action' => 'add_friend', 'method' => 'POST', 'id' => true),
    array('action' => 'unfriend', 'method' => 'POST', 'id' => true),
    array('action' => 'confirm_friend', 'method' => 'POST', 'id' => false),
    array('action' => 'mobile_users', 'method' => 'GET', 'id' => false),
    array('action' => 'developers', 'method' => 'GET', 'id' => false),
    array('action' => 'search_users', 'method' => 'GET', 'id' => false),
    array('action' => 'search_applications', 'method' => 'GET', 'id' => false),
    array('action' => 'search_groups', 'method' => 'GET', 'id' => false),
));
/*mapResources to Controllers*/

	Router::mapResources('users');
	Router::mapResources('applications');
	Router::mapResources('groups');
	Router::mapResources('notifications');
	Router::mapResources('user_followers');
	Router::mapResources('user_friends');
    Router::mapResources('search');
    Router::mapResources('comments');

	
	Router::parseExtensions('json');


/**
 * Here, we are connecting '/' (base path) to controller called 'Pages',
 * its action called 'display', and we pass a param to select the view file
 * to use (in this case, /app/View/Pages/home.ctp)...
 */
	Router::connect('/', array('controller'=>'sites', 'action'=>'index'));
	//Router::connect('/', array('controller' => 'pages', 'action' => 'display', 'home'));
/**
 * ...and connect the rest of 'Pages' controller's URLs.
 */
	Router::connect('/pages/*', array('controller' => 'pages', 'action' => 'display'));

/**
 * Load all plugin routes. See the CakePlugin documentation on
 * how to customize the loading of plugin routes.
 */
	CakePlugin::routes();

/**
 * Load the CakePHP default routes. Only remove this if you do not want to use
 * the built-in default routes.
 */
	require CAKE . 'Config' . DS . 'routes.php';
