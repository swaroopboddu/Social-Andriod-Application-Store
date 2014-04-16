<h3>Current System Status</h3>
<table class="table table-striped">
	<tr>
		<th>Name</th>
		<th>Email</th>
		<th>Role</th>
		<th>Date Joined</th>
		<th>Last Logined</th>
		<th>Actions</th>
	</tr>
	<?php foreach ($users as $user) { ?>
	<tr>
		<td style="min-width: 200px"><?php echo h($user['User']['first_name']." ".$user['User']['last_name']); ?></td>
		<td><?php echo h($user['User']['email']); ?></td>
		<td><?php echo h($user['User']['role']); ?></td>
		<td><?php echo h($user['User']['date_created']); ?></td>
		<td><?php echo h($user['User']['last_login']); ?></td>
		<td><?php echo $this->Form->postLink(__('Delete'), array('action' => 'delete', $user['User']['id']), null, __('Are you sure you want to delete # %s?', $user['User']['first_name'])); ?></td>
	</tr>
	<?php } ?>
	
</table>