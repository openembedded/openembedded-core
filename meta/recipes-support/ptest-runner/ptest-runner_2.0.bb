SUMMARY = "A C program to run all installed ptests"
DESCRIPTION = "The ptest-runner2 package installs a ptest-runner \
program which loops through all installed ptest test suites and \
runs them in sequence."
HOMEPAGE = "https://github.com/alimon/ptest-runner2"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=751419260aa954499f7abaabaa882bbe"

SRCREV = "8bfdf946e784f4d5757bebee1fbc4b4a3d7a95c4"
PV = "2.0+git${SRCPV}"

SRC_URI = "git://github.com/alimon/ptest-runner2.git;protocol=https"
S = "${WORKDIR}/git"

FILES_${PN} = "${bindir}/ptest-runner"

EXTRA_OEMAKE = "-e MAKEFLAGS="

do_compile () {
	oe_runmake
}

do_install () {
	install -D -m 0755 ${WORKDIR}/git/ptest-runner ${D}${bindir}/ptest-runner
}
