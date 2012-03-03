python do_pkg_write_metainfo () {
	deploydir = d.getVar('DEPLOY_DIR', True)
	if not deploydir:
		bb.error("DEPLOY_DIR not defined, unable to write package info")
		return

	try:
		infofile = file(os.path.join(deploydir, 'package-metainfo'), 'a')
	except OSError:
		raise bb.build.FuncFailed("unable to open package-info file for writing.")
	
	name = d.getVar('PN', True)
	version = d.getVar('PV', True)
	desc = d.getVar('DESCRIPTION', True)
	page = d.getVar('HOMEPAGE', True)
	lic = d.getVar('LICENSE', True)
	
	infofile.write("|| "+ name +" || "+ version + " || "+ desc +" || "+ page +" || "+ lic + " ||\n" ) 
	infofile.close()
}

addtask pkg_write_metainfo after do_package before do_build