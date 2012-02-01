SUMMARY = "Provider of the machine specific securetty file"
DESCRIPTION = "Provider of the machine specific securetty file"
SECTION = "base utils"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

INHIBIT_DEFAULT_DEPS = "1"

PR = "r1"

SRC_URI = "file://securetty"

# Since we deduce our arch from ${SERIAL_CONSOLE}
PACKAGE_ARCH = "${MACHINE_ARCH}"

do_install () {
	# Ensure we add a suitable securetty file to the package that has
	# most common embedded TTYs defined.
	if [ ! -z "${SERIAL_CONSOLE}" ]; then
		# Our SERIAL_CONSOLE contains a baud rate and sometimes a -L
		# option as well. The following pearl :) takes that and converts
		# it into newline-separated tty's and appends them into
		# securetty. So if a machine has a weird looking console device
		# node (e.g. ttyAMA0) that securetty does not know, it will get
		# appended to securetty and root logins will be allowed on that
		# console.
		echo "${SERIAL_CONSOLE}" | sed -e 's/[0-9][0-9]\|\-L//g'|tr "[ ]" "[\n]"  >> ${WORKDIR}/securetty
	fi
	install -d ${D}${sysconfdir}
	install -m 0400 ${WORKDIR}/securetty ${D}${sysconfdir}/securetty 
}
