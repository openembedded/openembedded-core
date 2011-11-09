python do_pkg_write_metainfo () {
	deploydir = d.getVar('DEPLOY_DIR', 1)
	if not deploydir:
		bb.error("DEPLOY_DIR not defined, unable to write package info")
		return

	try:
		infofile = file(os.path.join(deploydir, 'package-metainfo'), 'a')
	except OSError:
		raise bb.build.FuncFailed("unable to open package-info file for writing.")
	
	name = d.getVar('PN', 1)
	version = d.getVar('PV', 1)
	desc = d.getVar('DESCRIPTION', 1)
	page = d.getVar('HOMEPAGE', 1)
	lic = d.getVar('LICENSE', 1)
	
	infofile.write("|| "+ name +" || "+ version + " || "+ desc +" || "+ page +" || "+ lic + " ||\n" ) 
	infofile.close()
}

addtask pkg_write_metainfo after do_package before do_build