inherit src_distribute

# SRC_DIST_LOCAL possible values:
# copy		copies the files from ${A} to the distributedir
# symlink	symlinks the files from ${A} to the distributedir
# move+symlink	moves the files into distributedir, and symlinks them back
SRC_DIST_LOCAL ?= "move+symlink"
SRC_DISTRIBUTEDIR ?= "${DEPLOY_DIR}/sources"
SRC_DISTRIBUTECOMMAND () {
	s="${SRC}"
	if [ ! -L "$s" ] && (echo "$s"|grep "^${DL_DIR}"); then
		:
	else
		exit 0;
	fi
	mkdir -p ${SRC_DISTRIBUTEDIR}
	case "${SRC_DIST_LOCAL}" in
		copy)
			test -e $s.md5 && cp -f $s.md5 ${SRC_DISTRIBUTEDIR}/
			cp -f $s ${SRC_DISTRIBUTEDIR}/
			;;
		symlink)
			test -e $s.md5 && ln -sf $s.md5 ${SRC_DISTRIBUTEDIR}/
			ln -sf $s ${SRC_DISTRIBUTEDIR}/
			;;
		move+symlink)
			mv $s ${SRC_DISTRIBUTEDIR}/
			ln -sf ${SRC_DISTRIBUTEDIR}/`basename $s` $s
			;;
	esac
}
