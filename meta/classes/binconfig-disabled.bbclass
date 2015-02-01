#
# Class to disable binconfig files instead of installing them
#

# The list of scripts which should be disabled.
BINCONFIG ?= ""

FILES_${PN}-dev += "${bindir}/*-config"

do_install_append () {
	for x in ${BINCONFIG}; do
		echo "#!/bin/sh" > ${D}$x
		# Make the disabled script emit invalid parameters for those configure
		# scripts which call it without checking the return code.
		echo "echo '--should-not-have-used-$x'" >> ${D}$x
		echo "exit 1" >> ${D}$x
	done
}

SYSROOT_PREPROCESS_FUNCS += "binconfig_disabled_sysroot_preprocess"

binconfig_disabled_sysroot_preprocess () {
	for x in ${BINCONFIG}; do
		configname=`basename $x`
		install -d ${SYSROOT_DESTDIR}${bindir_crossscripts}
		install ${D}$x ${SYSROOT_DESTDIR}${bindir_crossscripts}
	done
}
