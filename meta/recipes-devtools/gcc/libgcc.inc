INHIBIT_DEFAULT_DEPS = "1"

DEPENDS = "virtual/${TARGET_PREFIX}gcc virtual/${TARGET_PREFIX}g++"

PACKAGES = "\
  ${PN} \
  ${PN}-dev \
  ${PN}-dbg \
  libgcov-dev \
  "

FILES_${PN} = "${base_libdir}/libgcc*.so.*"
FILES_${PN}-dev = " \
  ${base_libdir}/libgcc*.so \
  ${libdir}/${TARGET_SYS}/${BINV}/*crt* \
  ${libdir}/${TARGET_SYS}/${BINV}/64 \
  ${libdir}/${TARGET_SYS}/${BINV}/32 \
  ${libdir}/${TARGET_SYS}/${BINV}/x32 \
  ${libdir}/${TARGET_SYS}/${BINV}/n32 \
  ${libdir}/${TARGET_SYS}/${BINV}/libgcc*"
FILES_libgcov-dev = " \
  ${libdir}/${TARGET_SYS}/${BINV}/libgcov.a \
  "
FILES_${PN}-dbg += "${base_libdir}/.debug/"

do_configure () {
	target=`echo ${MULTIMACH_TARGET_SYS} | sed -e s#-${SDKPKGSUFFIX}##`
	install -d ${D}${base_libdir} ${D}${libdir}
	cp -fpPR ${STAGING_INCDIR_NATIVE}/gcc-build-internal-$target/* ${B}
	mkdir -p ${B}/${BPN}
	cd ${B}/${BPN}
	chmod a+x ${S}/${BPN}/configure
	${S}/${BPN}/configure ${CONFIGUREOPTS} ${EXTRA_OECONF}
}

do_compile () {
	target=`echo ${TARGET_SYS} | sed -e s#-${SDKPKGSUFFIX}##`
	cd ${B}/${BPN}
	oe_runmake MULTIBUILDTOP=${B}/$target/${BPN}/
}

do_install () {
	target=`echo ${TARGET_SYS} | sed -e s#-${SDKPKGSUFFIX}##`
	cd ${B}/${BPN}
	oe_runmake 'DESTDIR=${D}' MULTIBUILDTOP=${B}/$target/${BPN}/ install

	# Move libgcc_s into /lib
	mkdir -p ${D}${base_libdir}
	if [ -f ${D}${libdir}/nof/libgcc_s.so ]; then
		mv ${D}${libdir}/nof/libgcc* ${D}${base_libdir}
	else
		mv ${D}${libdir}/libgcc* ${D}${base_libdir} || true
	fi

	# install the runtime in /usr/lib/ not in /usr/lib/gcc on target
	# so that cross-gcc can find it in the sysroot

	mv ${D}${libdir}/gcc/* ${D}${libdir}
	rm -rf ${D}${libdir}/gcc/
	# unwind.h is installed here which is shipped in gcc-cross
	# as well as target gcc and they are identical so we dont
	# ship one with libgcc here
	rm -rf ${D}${libdir}/${TARGET_SYS}/${BINV}/include
}

do_package[depends] += "virtual/${MLPREFIX}libc:do_packagedata"
do_package_write_ipk[depends] += "virtual/${MLPREFIX}libc:do_packagedata"
do_package_write_deb[depends] += "virtual/${MLPREFIX}libc:do_packagedata"
do_package_write_rpm[depends] += "virtual/${MLPREFIX}libc:do_packagedata"

BBCLASSEXTEND = "nativesdk"

INSANE_SKIP_${PN}-dev = "staticdev"
INSANE_SKIP_${MLPREFIX}libgcov-dev = "staticdev"

addtask multilib_install after do_install before do_package do_populate_sysroot
# this makes multilib gcc files findable for target gcc
# e.g.
#    /usr/lib/i586-pokymllib32-linux/4.7/
# by creating this symlink to it
#    /usr/lib64/x86_64-poky-linux/4.7/32

python do_multilib_install() {
    import re

    multilibs = d.getVar('MULTILIB_VARIANTS', True)
    if not multilibs or bb.data.inherits_class('nativesdk', d):
        return

    binv = d.getVar('BINV', True)

    mlprefix = d.getVar('MLPREFIX', True)
    if ('%slibgcc' % mlprefix) != d.getVar('PN', True):
        return

    if mlprefix:
        orig_tune = d.getVar('DEFAULTTUNE_MULTILIB_ORIGINAL', True)
        orig_tune_params = get_tune_parameters(orig_tune, d)
        orig_tune_baselib = orig_tune_params['baselib']
        orig_tune_bitness = orig_tune_baselib.replace('lib', '')
        if not orig_tune_bitness:
            orig_tune_bitness = '32'

        src = '../../../' + orig_tune_baselib + '/' + \
            d.getVar('TARGET_SYS_MULTILIB_ORIGINAL', True) + '/' + binv + '/'

        dest = d.getVar('D', True) + d.getVar('libdir', True) + '/' + \
            d.getVar('TARGET_SYS', True) + '/' + binv + '/' + orig_tune_bitness

        if os.path.lexists(dest):
            os.unlink(dest)
        os.symlink(src, dest)
        return


    for ml in multilibs.split():
        tune = d.getVar('DEFAULTTUNE_virtclass-multilib-' + ml, True)
        if not tune:
            bb.warn('DEFAULTTUNE_virtclass-multilib-%s is not defined. Skipping...' % ml)
            continue

        tune_parameters = get_tune_parameters(tune, d)
        tune_baselib = tune_parameters['baselib']
        if not tune_baselib:
            bb.warn("Tune %s doesn't have a baselib set. Skipping..." % tune)
            continue

        tune_arch = tune_parameters['arch']
        tune_bitness = tune_baselib.replace('lib', '')
        if not tune_bitness:
            tune_bitness = '32' # /lib => 32bit lib

        src = '../../../' + tune_baselib + '/' + \
            tune_arch + d.getVar('TARGET_VENDOR', True) + 'ml' + ml + \
            '-' + d.getVar('TARGET_OS', True) + '/' + binv + '/'

        dest = d.getVar('D', True) + d.getVar('libdir', True) + '/' + \
            d.getVar('TARGET_SYS', True) + '/' + binv + '/' + tune_bitness

        if os.path.lexists(dest):
            os.unlink(dest)
        os.symlink(src, dest)
}
