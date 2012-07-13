SUMMARY = "LiSt Open Files tool"
DESCRIPTION = "Lsof is a Unix-specific diagnostic tool. \
Its name stands for LiSt Open Files, and it does just that."
SECTION = "devel"
LICENSE = "BSD"
PR = "r1"

SRC_URI = "ftp://lsof.itap.purdue.edu/pub/tools/unix/lsof/lsof_${PV}.tar.bz2"

SRC_URI[md5sum] = "102ee2081172bbe76dccaa6cceda8573"
SRC_URI[sha256sum] = "49aa58e63539c45bada514a6a2e5a1c9f946ada1f4137dc9154cf4bf6054a1c1"

LOCALSRC = "file://${WORKDIR}/lsof_${PV}/lsof_${PV}_src.tar"
S = "${WORKDIR}/lsof_${PV}_src"

LIC_FILES_CHKSUM = "file://${S}/00README;beginline=645;endline=679;md5=e0108f7811919035515a97f872eb8ee2"

python do_unpack () {
    bb.build.exec_func('base_do_unpack', d)
    src_uri = d.getVar('SRC_URI')
    d.setVar('SRC_URI', '${LOCALSRC}')
    bb.build.exec_func('base_do_unpack', d)
    d.setVar('SRC_URI', src_uri)
}

export LSOF_OS = "${TARGET_OS}"
LSOF_OS_libc-uclibc = "linux"
LSOF_OS_libc-glibc = "linux"
export LSOF_INCLUDE = "${STAGING_INCDIR}"

do_configure () {
        if [ "x${EGLIBCVERSION}" != "x" ];then
                LINUX_CLIB=`echo ${EGLIBCVERSION} |sed -e 's,\.,,g'`
                LINUX_CLIB="-DGLIBCV=${LINUX_CLIB}"
                export LINUX_CLIB
        fi
	yes | ./Configure ${LSOF_OS}
}

export I = "${STAGING_INCDIR}"
export L = "${STAGING_INCDIR}"
export EXTRA_OEMAKE = ""

do_compile () {
	oe_runmake 'CC=${CC}' 'CFGL=${LDFLAGS} -L./lib -llsof' 'DEBUG=' 'INCL=${CFLAGS}'
}

do_install () {
	install -d ${D}${sbindir} ${D}${mandir}/man8
	install -m 4755 lsof ${D}${sbindir}/lsof
	install -m 0644 lsof.8 ${D}${mandir}/man8/lsof.8
}
