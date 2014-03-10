<?php
/**
 *
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
 * @package       app.View.Layouts
 * @since         CakePHP(tm) v 0.10.0.1076
 * @license       http://www.opensource.org/licenses/mit-license.php MIT License
 */

?>
<!DOCTYPE html>
<html>
<head>
<?php echo $this->Html->charset(); ?>
<title>Secure Social App Store</title>
<?php
echo $this->Html->meta('icon');

echo $this->Html->css('cake.generic');
echo $this->Html->css('bootstrap');

echo $this->fetch('meta');
echo $this->fetch('css');
echo $this->fetch('script');
?>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<?php echo $this->Html->script('bootstrap.js') ?>
</head>
<body>
	<div class="container">
		<header id="header">
			<div class = "row-fluid">
			<div class = "span9">
			<?php echo $this->Html->image('ss_logo.png', array("alt" => "Secure Social",
	 														'url' => array('controller' => 'sites', 'action'=>'index'))); ?>
			</div>
			<div class = "span3">
		<?php
			if($this->Session->check('User.id')) {
				echo "welcome, ";
				echo $this->Html->link($this->Session->read('User.first_name'), array('controller' => 'users', 'action' => 'view', $this->Session->read('User.id'))); ?>
				|
				<?php echo $this->Html->link('Logout', array('controller'=>'users', 'action'=>'logout'), array('type'=>'button', 'class' => 'btn btn-primary'));
			} else {
				echo $this->Html->link('Login', array('controller'=>'users', 'action'=>'login'));
			}
		 ?>
		</div>
	</div>
		</header>
		<div id="content">

		<?php echo $this->Session->flash(); ?>
		<?php echo $this->Session->flash('auth'); ?>
		
		<?php echo $this->fetch('content'); ?>
		</div>
		<div id="footer">
		</div>
	</div>
	<?php //echo $this->element('sql_dump'); ?>
</body>
</html>