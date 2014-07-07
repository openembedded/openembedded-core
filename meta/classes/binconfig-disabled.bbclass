#
# Class to disable binconfig files instead of installing them
#

# The list of scripts which should be disabled.
BINCONFIG ?= ""

FILES_${PN}-dev += "${bindir}/*-config"

do_install_append () {
	for x in ${BINCONFIG}; do
		echo "#!/bin/sh" > ${D}$x
		echo "exit 1" >> ${D}$x
	done
}
