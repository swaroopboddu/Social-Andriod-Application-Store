<?php //pr($application); ?>
<div class="row">
	<div class="col-md-3 actions">
		<h3><?php echo __('Actions'); ?></h3>
		<ul>
			<?php echo $this->Html->link(__('Download'), array('action' => 'download_app', $application['Application']['id']), array('type'=>'button', 'class'=>'btn btn-default')); ?> 
			<br></br>
			<?php if($application['User']['id'] == $this->Session->read('User.id')) { ?>
			<?php echo $this->Html->link(__('Edit Application'), array('action' => 'edit', $application['Application']['id']), array('type'=>'button', 'class'=>'btn btn-warning')); ?> 
			<br></br>
			<?php //echo $this->Form->postLink(__('Delete Application'), array('action' => 'delete', $application['Application']['id']), array('type'=>'button', 'class'=>'btn btn-danger'),null, __('Are you sure you want to delete # %s?', $application['Application']['id'])); ?> 
			<?php } ?>
		</ul>
	</div>
	<div class="col-md-9 applications view">
	<h2><?php echo __('Application'); ?></h2>
		<dl class="dl-horizontal">
			<!-- <dt><?php echo __('Id'); ?></dt>
			<dd>
				<?php echo h($application['Application']['id']); ?>
				&nbsp;
			</dd>
			<dt><?php echo __('User Id'); ?></dt>
			<dd>
				<?php echo h($application['Application']['user_id']); ?>
				&nbsp;
			</dd> -->
			<dt><?php echo __('Title'); ?></dt>
			<dd>
				<?php echo h($application['Application']['title']); ?>
				&nbsp;
			</dd>
			<dt><?php echo __('Developer'); ?></dt>
			<dd>
				<?php echo $application['User']['first_name']." ".$application['User']['last_name']; ?>
				&nbsp;
			</dd>
			<dt><?php echo __('Description'); ?></dt>
			<dd>
				<?php echo $application['Application']['description']; ?>
				&nbsp;
			</dd>
			<dt><?php echo __('Size'); ?></dt>
			<dd>
				<?php echo $application['ApplicationRevision'][0]['size']." "."KB"; ?>
				&nbsp;
			</dd>
			<dt><?php echo __('No Of Users Rated'); ?></dt>
			<dd>
				<?php echo h($application['Application']['count_rating']); ?>
				&nbsp;
			</dd>
			<dt><?php echo __('Rating'); ?></dt>
			<dd>
				<?php echo h($application['Application']['rating']); ?>
				&nbsp;
			</dd>
		</dl>
	</div>
</div>

<div class="row" style="margin-top: 20px">
	<div class="col-md-7">
		<h4><i>Comments</i></h4>
		<div class="comments">
			<?php foreach ($comments as $comment) { ?>

				<div class="comment">
					<b><?php echo h($comment['User']['first_name']." ".$comment['User']['last_name']); ?></b>
					<span class="comment-date"><?php echo h(date('M d Y', strtotime($comment['comment']['posted_date']))); ?></span>
					<br>
					<?php echo h($comment['comment']['description']); ?>
				</div>
				<!-- pr($comment);
				pr($comment['comment']['description']); -->
			<?php } ?>
		</div>
	</div>
		<!-- <table class="table table-striped">
			<?php foreach ($comments as $comment) {
				pr($comment);
				pr($comment['comment']['description']);
			} ?>
		</table> -->
	<div class="col-md-5">	
		<div class="comments form">
			<?php $options = array('div' => array('class' => 'form-group'), 'class'=>'form-control'); ?>
			<?php echo $this->Form->create('Comment', array('url' => array(
				'controller' => 'comments', 'action' => 'add'))); ?>
			<fieldset>
				<h4><i>Add Comment</i></h4>
			<?php
				echo $this->Form->input('application_id',array(
					'value' => $application['Application']['id'], 'type' => 'hidden'));
				echo $this->Form->input('description',array('label' => false, 'type' => 'textarea', 'cols' => 68,'rows' => 3, 'max-length' => 100),$options);
				echo $this->Form->submit('Comment', array('div' => false, 'style' => array('float: right;'),'type'=>'button', 'class'=>'btn btn-primary', 'onclick'=>'submit();'));
			?>
			</fieldset>
		<?php echo $this->Form->end(); ?>
		</div>
	</div>
</div>
