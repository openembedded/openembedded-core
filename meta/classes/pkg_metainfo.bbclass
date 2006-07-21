python do_pkg_write_metainfo () {
	deploydir = bb.data.getVar('DEPLOY_DIR', d, 1)
	if not deploydir:
		bb.error("DEPLOY_DIR not defined, unable to write package info")
		return

	try:
		infofile = file(os.path.join(deploydir, 'package-metainfo'), 'a')
	except OSError:
		raise bb.build.FuncFailed("unable to open package-info file for writing.")
	
	name = bb.data.getVar('PN', d, 1)
	version = bb.data.getVar('PV', d, 1)
	desc = bb.data.getVar('DESCRIPTION', d, 1)
	page = bb.data.getVar('HOMEPAGE', d, 1)
	lic = bb.data.getVar('LICENSE', d, 1)
	
	infofile.write("|| "+ name +" || "+ version + " || "+ desc +" || "+ page +" || "+ lic + " ||\n" ) 
	infofile.close()
}

addtask pkg_write_metainfo after do_package before do_build