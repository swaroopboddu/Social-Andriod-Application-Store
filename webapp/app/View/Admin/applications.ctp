<h3>Current Applications</h3>
<table class="table table-striped">
	<tr>
		<th>Title</th>
		<th>Developer</th>
		<th>Description</th>
		<th>No of users</th>
		<th>Rating</th>
		<th>Actions</th>
	</tr>
	<?php
			$count = 1; 
			$app_rating = array();
		?>
	<?php foreach ($applications as $application) { ?>
	<tr>
		<td><?php echo $this->Html->link($application['Application']['title'], array('controller' =>'applications', 'action' => 'view', $application['Application']['id'])); ?></td>
		<td><?php echo h($application['User']['first_name']); ?>&nbsp;</td>
		<td style="max-width: 500px"><?php 
				if(strlen($application['Application']['description']) >= 200) {
						echo h(substr($application['Application']['description'],0,200)." ... ");
					}  else {
						echo h($application['Application']['description']);
					}
			 ?>&nbsp;</td>
		<td><?php echo h($application['Application']['count_rating']); ?>&nbsp;</td>
		<td><?php echo h($application['Application']['rating']); ?>&nbsp;</td>
		<!-- <td><?php echo $this->Html->link(null, array(), array('id' => 'rating'.$count));
					array_push($app_rating, $application['Application']['rating']); ?>&nbsp;</td> -->
		<td class="actions">
			<?php echo $this->Form->postLink(__('Delete'), array('action' => 'delete', $application['Application']['id']), null, __('Are you sure you want to delete # %s?', $application['Application']['title'])); ?>
		</td>
	</tr>
		<?php $count = $count+1; ?>
	<?php } ?>
	
</table>
<?php echo $this->Html->script('jquery.raty.js')?>
<script type="text/javascript">
var app_ratings = new Array();
<?php foreach($app_rating as $rating): ?>
	app_ratings.push('<?php echo $rating; ?>');
<?php endforeach; ?>
 for(var i=1;i<"<?php echo $count; ?>";i++) {
	var id = '#rating'+i;
	$(id).raty({
		score: app_ratings[i-1],
		path: 'img'
	});
 }
</script>