require rsync.inc

PR = "r0"

SRC_URI += "file://acinclude.m4"

SRC_URI[md5sum] = "5ee72266fe2c1822333c407e1761b92b"
SRC_URI[sha256sum] = "30f10f8dd5490d28240d4271bb652b1da7a60b22ed2b9ae28090668de9247c05"

EXTRA_OECONF += "--disable-xattr-support --disable-acl-support"

# rsync 3.0 uses configure.sh instead of configure, and
# makefile checks the existence of configure.sh
do_configure_prepend () {
	rm -f ${S}/configure ${S}/configure.sh
	cp -f ${WORKDIR}/acinclude.m4 ${S}/

	# by default, if crosscompiling, rsync                                                           
	# disables a number of capabilities, hardlinking                                                 
	# symlinks and special files (ie devices)
	export rsync_cv_can_hardlink_special=yes
	export rsync_cv_can_hardlink_symlink=yes 
}

do_configure_append () {
	cp -f ${S}/configure ${S}/configure.sh
}
