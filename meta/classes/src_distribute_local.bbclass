inherit src_distribute

# SRC_DIST_LOCAL possible values:
# copy		copies the files to the distributedir
# symlink	symlinks the files to the distributedir
# move+symlink	moves the files into distributedir, and symlinks them back
SRC_DIST_LOCAL ?= "move+symlink"
SRC_DISTRIBUTEDIR ?= "${DEPLOY_DIR}/sources"
SRC_DISTRIBUTECOMMAND () {
	s="${SRC}"
	d="${DEST}"

	mkdir -p ${SRC_DISTRIBUTEDIR}

	if echo $d | grep -q '/$'; then
		mkdir -p ${SRC_DISTRIBUTEDIR}/$d
	fi

	case "${SRC_DIST_LOCAL}" in
		copy)
			test -e $s.md5 && cp -f $s.md5 ${SRC_DISTRIBUTEDIR}/$d.md5
			cp -f $s ${SRC_DISTRIBUTEDIR}/$d
			;;
		symlink)
			test -e $s.md5 && ln -sf $s.md5 ${SRC_DISTRIBUTEDIR}/$d.md5
			ln -sf $s ${SRC_DISTRIBUTEDIR}/$d
			;;
		move+symlink)
			mv $s ${SRC_DISTRIBUTEDIR}/$d
			ln -sf ${SRC_DISTRIBUTEDIR}/$d $s
			;;
	esac
}
