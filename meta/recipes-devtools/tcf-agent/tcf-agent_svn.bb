DESCRIPTION = "Target Communication Framework"
HOMEPAGE = "http://dsdp.eclipse.org/dsdp/tm/"
BUGTRACKER = "https://bugs.eclipse.org/bugs/"

LICENSE = "EPLv1.0 | EDLv1.0"
LIC_FILES_CHKSUM = "file://../epl-v10.html;md5=7aa4215a330a0a4f6a1cbf8da1a0879f \
                    file://../agent/edl-v10.html;md5=522a390a83dc186513f0500543ad3679"

PV = "0.3.0+svnr${SRCPV}"
PR = "r0"

SRC_URI = "svn://dev.eclipse.org/svnroot/dsdp/org.eclipse.tm.tcf/;module=tags/0.3.0/;proto=http \
           file://terminals_agent.patch \
           file://fix_tcf-agent.init.patch"

S = "${WORKDIR}/tags/0.3.0/tcf-agent"

inherit update-rc.d

INITSCRIPT_NAME = "tcf-agent"
INITSCRIPT_PARAMS = "start 999 3 5 . stop 20 0 1 2 6 ."

# mangling needed for make
MAKE_ARCH = `echo ${TARGET_ARCH} | sed s,i.86,i686,`
MAKE_OS = `echo ${TARGET_OS} | sed s,linux,GNU/Linux,`

EXTRA_OEMAKE = "MACHINE=${MAKE_ARCH} OPSYS=${MAKE_OS} 'CC=${CC}' 'AR=${AR}'"

do_compile() {
	oe_runmake
}

do_install() {
	oe_runmake install INSTALLROOT=${D}
}

