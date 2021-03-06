<#ftl output_format='HTML'>
<!DOCTYPE html>
<html>
<head>
<title>${getText('upload')}</title>
</head>
<body>
<@s.form id="upload_form" action=actionBaseUrl method="post" class="ajax view" data\-replacement="files">
	<#if limit??><@s.hidden name="limit"/></#if>
	<input type="hidden" name="pick" value="true"/>
	<table id="files" class="table table-striped middle" style="margin-top:50px;">
		<caption style="font-size:120%;font-weight:bold;line-height:30px;"><@s.hidden id="folder" name="folder"/>${getText('current.location')}:<span id="current_folder" style="margin-left:10px;">${folder}<#if !folder?ends_with('/')>/</#if></span></caption>
		<thead>
		<tr style="font-weight:bold;height:43px;">
			<td style="width:50px" class="radio"></td>
			<td style="width:280px;"><span style="line-height:28px;">${getText('name')}</span><input type="search" class="filter input-small pull-right"/></td>
			<td class="center">${getText('preview')}</td>
		</tr>
		</thead>
		<tfoot>
		<#if pagedFiles?? && (marker?has_content || pagedFiles.nextMarker??)>
		<tr>
			<td colspan="4" class="center">
			<#if marker?has_content> 
			<a class="ajax view" data-replacement="files" href="${actionBaseUrl}/pick${folderEncoded}?<#if limit??>limit=${limit}&</#if>marker=${previousMarker!}">${getText('previouspage')}</a>
			<#else>
			<span>${getText('previouspage')}</span>
			</#if>
			<#if pagedFiles.nextMarker??>
			<a class="ajax view" data-replacement="files" href="${actionBaseUrl}/pick${folderEncoded}?<#if limit??>limit=${limit}&</#if><#if pagedFiles.marker??>previousMarker=${pagedFiles.marker}&</#if>marker=${pagedFiles.nextMarker}">${getText('nextpage')}</a>
			<#else>
			<span>${getText('nextpage')}</span>
			</#if>
			</td>
		</tr>
		</#if>
		<tr>
			<td colspan="3" class="center">
			<button type="button" class="btn mkdir">${getText('create.subfolder')}</button>
			<button type="button" class="btn snapshot">${getText('snapshot')}</button>
			<button type="button" class="btn reload">${getText('reload')}</button>
			</td>
		</tr>
		</tfoot>
		<tbody>
		<#if !files?? && pagedFiles??><#assign files = pagedFiles.result></#if>
		<#list files as f>
		<#assign key = f.name>
		<#assign value = f.file>
		<tr>
			<td class="radio"><#if value><input type="radio" name="id" value="<@url value="${action.getFileUrl(key?url)}"/>"/></#if></td>
			<td><#if value><span class="uploaditem filename" style="cursor:pointer;">${key}</span> <a href="<@url value="${action.getFileUrl(key?url)}"/>" target="_blank" download="${key}"><i class="glyphicon glyphicon-download-alt clickable"></i></a><#else><a style="color:blue;" class="ajax view" data-replacement="files" href="${actionBaseUrl}/pick${folderEncoded}/${key?replace('..','__')?url}<#if limit??>?limit=${limit}</#if>">${key}</a></#if></td>
			<td class="center"><#if value && ['jpg','gif','png','webp','bmp']?seq_contains(key?keep_after_last('.')?lower_case)><a href="<@url value="${action.getFileUrl(key?url)}"/>" target="_blank"><img class="uploaditem" src="<@url value="${action.getFileUrl(key?url)}"/>" style="height:50px;"/></a></#if></td>
		</tr>
		</#list>
		</tbody>
	</table>
</@s.form>
</body>
</html>


