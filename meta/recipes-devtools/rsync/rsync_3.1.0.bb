require rsync.inc


SRC_URI += "file://acinclude.m4 \
	    file://check_libattr.patch"

SRC_URI[md5sum] = "3be148772a33224771a8d4d2a028b132"
SRC_URI[sha256sum] = "81ca23f77fc9b957eb9845a6024f41af0ff0c619b7f38576887c63fa38e2394e"

PACKAGECONFIG ??= "acl attr"
PACKAGECONFIG[acl] = "--enable-acl-support,--disable-acl-support,acl,"
PACKAGECONFIG[attr] = "--enable-xattr-support,--disable-xattr-support,attr,"

# rsync 3.0 uses configure.sh instead of configure, and
# makefile checks the existence of configure.sh
do_configure_prepend () {
	rm -f ${S}/configure ${S}/configure.sh
	cp -f ${WORKDIR}/acinclude.m4 ${S}/

	# By default, if crosscompiling, rsync disables a number of
	# capabilities, hardlinking symlinks and special files (i.e. devices)
	export rsync_cv_can_hardlink_special=yes
	export rsync_cv_can_hardlink_symlink=yes
}

do_configure_append () {
	cp -f ${S}/configure ${S}/configure.sh
}
