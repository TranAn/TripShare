/**
 * @license Copyright (c) 2003-2015, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	config.uiColor = '#f6f7f8';
	config.extraPlugins = 'blockimagepaste';
	config.removePlugins = 'autogrow';
	config.height = 180;
	config.resize_minHeight = 180;
	config.resize_maxHeight = 250;
	config.toolbar = [
		         		{ name: 'document', items: [ 'Source', '-', 'Undo', 'Redo'] },
		        		[ 'Bold', 'Italic', 'Underline', '-', 'RemoveFormat'],
		        		[ 'Find', 'Replace'],
		        		[ 'JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock'],
		        		[ 'Link', 'Unlink'],
		        		[ 'Image', 'Smiley'],
		        		[ 'Styles'],
		        		[ 'Format'],
		        		[ 'Font'],
		        		[ 'FontSize'],
		         		[ 'TextColor', 'Maximize' ]
	         		]
};
