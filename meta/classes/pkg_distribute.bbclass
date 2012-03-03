PKG_DISTRIBUTECOMMAND[func] = "1"
python do_distribute_packages () {
	cmd = d.getVar('PKG_DISTRIBUTECOMMAND', True)
	if not cmd:
		raise bb.build.FuncFailed("Unable to distribute packages, PKG_DISTRIBUTECOMMAND not defined")
	bb.build.exec_func('PKG_DISTRIBUTECOMMAND', d)
}

addtask distribute_packages before do_build after do_fetch

PKG_DIST_LOCAL ?= "symlink"
PKG_DISTRIBUTEDIR ?= "${DEPLOY_DIR}/packages"

PKG_DISTRIBUTECOMMAND () {
	p=`dirname ${FILE}`
	d=`basename $p`
	mkdir -p ${PKG_DISTRIBUTEDIR}
	case "${PKG_DIST_LOCAL}" in
		copy)
			# use this weird tar command to copy because we want to 
			# exclude the BitKeeper directories
			test -e ${PKG_DISTRIBUTEDIR}/${d} || mkdir ${PKG_DISTRIBUTEDIR}/${d};
			(cd ${p}; tar -c --exclude SCCS -f - . ) | tar -C ${PKG_DISTRIBUTEDIR}/${d} -xpf -
			;;
		symlink)
			ln -sf $p ${PKG_DISTRIBUTEDIR}/
			;;
	esac
}
