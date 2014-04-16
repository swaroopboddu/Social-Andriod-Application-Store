<div class="col-md-7">
	<div class="update form">
	<?php //$options = array('div' => array('class' => 'form-group'), 'class'=>'form-control'); ?>
	<?php echo $this->Form->create('Application', array('url' => array('controller' => 'admin', 'action' => 'update'))); ?>
		<fieldset>
			<legend><?php echo __('Push Notifications'); ?></legend>
		<?php
			echo $this->Form->input('Users', array('type' => 'select','options' => $options, 'div' => array('class' => 'form-group'), 'class'=>'form-control'));
			echo $this->Form->input('Message', array('type' => 'textarea','cols' => 60, 'rows' => 10,'div' => array('class' => 'form-group'), 'class'=>'form-control')); ?>
			<br>
		<?php echo $this->Form->submit('Save', array('type' => 'button', 'div'=>false, 'class'=>'btn btn-success', 'onclick'=>'submit();'));

		?>
		</fieldset>
	<?php echo $this->Form->end(); ?>
	</div>
</div>