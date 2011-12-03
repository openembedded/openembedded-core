DESCRIPTION = "Target Communication Framework"
HOMEPAGE = "http://wiki.eclipse.org/TCF"
BUGTRACKER = "https://bugs.eclipse.org/bugs/"

LICENSE = "EPL-1.0 | EDL-1.0"
LIC_FILES_CHKSUM = "file://../epl-v10.html;md5=7aa4215a330a0a4f6a1cbf8da1a0879f \
                    file://edl-v10.html;md5=522a390a83dc186513f0500543ad3679"

SRCREV = "1855"
PV = "0.0+svnr${SRCPV}"
PR = "r3"

SRC_URI = "svn://dev.eclipse.org/svnroot/dsdp/org.eclipse.tm.tcf/trunk;module=agent;proto=http \
           http://dev.eclipse.org/svnroot/dsdp/org.eclipse.tm.tcf/trunk/epl-v10.html;name=epl \
           file://fix_ranlib.patch \
           file://fix_tcf-agent.init.patch \
          "

SRC_URI[epl.md5sum] = "7aa4215a330a0a4f6a1cbf8da1a0879f"
SRC_URI[epl.sha256sum] = "4fd64aeed340d62a64a8da4b371efe0f6d0d745f4d2dbefacba86c646d36bc72"

DEPENDS = "util-linux openssl"
RDEPENDS_${PN} = "bash"

S = "${WORKDIR}/agent"

inherit update-rc.d

INITSCRIPT_NAME = "tcf-agent"
INITSCRIPT_PARAMS = "start 999 3 5 . stop 20 0 1 2 6 ."

# mangling needed for make
MAKE_ARCH = `echo ${TARGET_ARCH} | sed s,i.86,i686,`
MAKE_OS = `echo ${TARGET_OS} | sed s,^linux.*,GNU/Linux,`

EXTRA_OEMAKE = "MACHINE=${MAKE_ARCH} OPSYS=${MAKE_OS} 'CC=${CC}' 'AR=${AR}'"

# They don't build on ARM and we don't need them actually.
CFLAGS += "-DSERVICE_RunControl=0 -DSERVICE_Breakpoints=0 \
    -DSERVICE_Memory=0 -DSERVICE_Registers=0 -DSERVICE_MemoryMap=0 \
    -DSERVICE_StackTrace=0 -DSERVICE_Symbols=0 -DSERVICE_LineNumbers=0 \
    -DSERVICE_Expressions=0"

do_compile() {
	oe_runmake
}

do_install() {
	oe_runmake install INSTALLROOT=${D}
}

