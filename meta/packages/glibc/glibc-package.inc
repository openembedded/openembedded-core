#
# For now, we will skip building of a gcc package if it is a uclibc one
# and our build is not a uclibc one, and we skip a glibc one if our build
# is a uclibc build.
#
# See the note in gcc/gcc_3.4.0.oe
#

python __anonymous () {
    import bb, re
    uc_os = (re.match('.*uclibc*', bb.data.getVar('TARGET_OS', d, 1)) != None)
    if uc_os:
        raise bb.parse.SkipPackage("incompatible with target %s" %
                                   bb.data.getVar('TARGET_OS', d, 1))
}


# Binary locales are generated at build time if ENABLE_BINARY_LOCALE_GENERATION
# is set. The idea is to avoid running localedef on the target (at first boot)
# to decrease initial boot time and avoid localedef being killed by the OOM
# killer which used to effectively break i18n on machines with < 128MB RAM.

# default to disabled until qemu works for everyone
ENABLE_BINARY_LOCALE_GENERATION ?= "0"

# BINARY_LOCALE_ARCHES is a space separated list of regular expressions
BINARY_LOCALE_ARCHES ?= "arm.*"


inherit glibc-package

def get_glibc_fpu_setting(bb, d):
    if bb.data.getVar('TARGET_FPU', d, 1) in [ 'soft' ]:
        return "--without-fp"
    return ""

EXTRA_OECONF += "${@get_glibc_fpu_setting(bb, d)}"
EXTRA_OEMAKE += "rootsbindir=${base_sbindir}"

OVERRIDES_append = ":${TARGET_ARCH}-${TARGET_OS}"

do_install() {
	oe_runmake install_root=${D} install
	for r in ${rpcsvc}; do
		h=`echo $r|sed -e's,\.x$,.h,'`
		install -m 0644 ${S}/sunrpc/rpcsvc/$h ${D}/${includedir}/rpcsvc/
	done
	install -m 0644 ${WORKDIR}/etc/ld.so.conf ${D}/${sysconfdir}/
	install -d ${D}${libdir}/locale
	make -f ${WORKDIR}/generate-supported.mk IN="${S}/localedata/SUPPORTED" OUT="${WORKDIR}/SUPPORTED"
	# get rid of some broken files...
	for i in ${GLIBC_BROKEN_LOCALES}; do
		grep -v $i ${WORKDIR}/SUPPORTED > ${WORKDIR}/SUPPORTED.tmp
		mv ${WORKDIR}/SUPPORTED.tmp ${WORKDIR}/SUPPORTED
	done
	rm -f ${D}/etc/rpc
	rm -f ${D}${includedir}/scsi/sg.h
	rm -f ${D}${includedir}/scsi/scsi_ioctl.h
	rm -f ${D}${includedir}/scsi/scsi.h
}


python __anonymous () {
    enabled = bb.data.getVar("ENABLE_BINARY_LOCALE_GENERATION", d, 1)

    if enabled and int(enabled):
        import re

        target_arch = bb.data.getVar("TARGET_ARCH", d, 1)
        binary_arches = bb.data.getVar("BINARY_LOCALE_ARCHES", d, 1) or ""

        for regexp in binary_arches.split(" "):
            r = re.compile(regexp)

            if r.match(target_arch):
                depends = bb.data.getVar("DEPENDS", d, 1)
                depends = "%s qemu-native" % depends
                bb.data.setVar("DEPENDS", depends, d)
                bb.data.setVar("GLIBC_INTERNAL_USE_BINARY_LOCALE", "compile", d)
                break
}



