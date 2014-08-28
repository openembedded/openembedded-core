SUMMARY = "LiSt Open Files tool"
DESCRIPTION = "Lsof is a Unix-specific diagnostic tool. \
Its name stands for LiSt Open Files, and it does just that."
SECTION = "devel"
LICENSE = "BSD"

SRC_URI = "ftp://lsof.itap.purdue.edu/pub/tools/unix/lsof/lsof_${PV}.tar.bz2"

SRC_URI[md5sum] = "80e2a76d0e05826db910ec88e631296c"
SRC_URI[sha256sum] = "dfdd3709d82bc79ccdf3e404b84aafa9aede5948642a824ecaefd0aac589da2c"

LOCALSRC = "file://${WORKDIR}/lsof_${PV}/lsof_${PV}_src.tar"
S = "${WORKDIR}/lsof_${PV}_src"

LIC_FILES_CHKSUM = "file://${S}/00README;beginline=645;endline=679;md5=964df275d26429ba3b39dbb9f205172a"

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
	export LSOF_AR="${AR} cr"
	export LSOF_RANLIB="${RANLIB}"
        if [ "x${GLIBCVERSION}" != "x" ];then
                LINUX_CLIB=`echo ${GLIBCVERSION} |sed -e 's,\.,,g'`
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
