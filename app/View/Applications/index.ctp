<div class="applications index">
	<h2><?php echo __('Applications'); ?></h2>
	<table cellpadding="0" cellspacing="0">
	<tr>
			<th><?php echo $this->Paginator->sort('id'); ?></th>
			<th><?php echo $this->Paginator->sort('user_id'); ?></th>
			<th><?php echo $this->Paginator->sort('description'); ?></th>
			<th><?php echo $this->Paginator->sort('count_rating'); ?></th>
			<th><?php echo $this->Paginator->sort('rating'); ?></th>
			<th class="actions"><?php echo __('Actions'); ?></th>
	</tr>
	<?php foreach ($applications as $application): ?>
	<tr>
		<td><?php echo h($application['Application']['id']); ?>&nbsp;</td>
		<td><?php echo h($application['Application']['user_id']); ?>&nbsp;</td>
		<td><?php echo h($application['Application']['description']); ?>&nbsp;</td>
		<td><?php echo h($application['Application']['count_rating']); ?>&nbsp;</td>
		<td><?php echo h($application['Application']['rating']); ?>&nbsp;</td>
		<td class="actions">
			<?php echo $this->Html->link(__('View'), array('action' => 'view', $application['Application']['id'])); ?>
			<?php echo $this->Html->link(__('Edit'), array('action' => 'edit', $application['Application']['id'])); ?>
			<?php echo $this->Form->postLink(__('Delete'), array('action' => 'delete', $application['Application']['id']), null, __('Are you sure you want to delete # %s?', $application['Application']['id'])); ?>
		</td>
	</tr>
<?php endforeach; ?>
	</table>
	<p>
	<?php
	echo $this->Paginator->counter(array(
	'format' => __('Page {:page} of {:pages}, showing {:current} records out of {:count} total, starting on record {:start}, ending on {:end}')
	));
	?>	</p>
	<div class="paging">
	<?php
		echo $this->Paginator->prev('< ' . __('previous'), array(), null, array('class' => 'prev disabled'));
		echo $this->Paginator->numbers(array('separator' => ''));
		echo $this->Paginator->next(__('next') . ' >', array(), null, array('class' => 'next disabled'));
	?>
	</div>
</div>
<div class="actions">
	<h3><?php echo __('Actions'); ?></h3>
	<ul>
		<li><?php echo $this->Html->link(__('New Application'), array('action' => 'add')); ?></li>
	</ul>
</div>
