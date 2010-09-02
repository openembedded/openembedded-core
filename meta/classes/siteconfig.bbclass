siteconfig_do_siteconfig() {
	if [ ! -d ${FILE_DIRNAME}/site_config ]; then
		exit 0
	fi
	mkdir ${WORKDIR}/site_config
	gen-site-config ${FILE_DIRNAME}/site_config \
		>${WORKDIR}/site_config/configure.ac
	cd ${WORKDIR}/site_config
	autoconf
        CONFIG_SITE="" ./configure ${CONFIGUREOPTS} --cache-file ${PN}_cache
	sed -n -e "/ac_cv_c_bigendian/p" -e "/ac_cv_sizeof_/p" \
		-e "/ac_cv_type_/p" -e "/ac_cv_header_/p" -e "/ac_cv_func_/p" \
		< ${PN}_cache > ${PN}_config
	mkdir -p ${SYSROOT_DESTDIR}${STAGING_DATADIR}/${TARGET_SYS}_config_site.d
	cp ${PN}_config ${SYSROOT_DESTDIR}${STAGING_DATADIR}/${TARGET_SYS}_config_site.d
}

# Ugly integration with sstate_task_postfunc for now.  The normal package 
# sysroot components must be installed in order to generate the cache, but
# the site cache must be generated before the staging archive is generated.
python sstate_task_postfunc () {
    shared_state = sstate_state_fromvars(d)
    sstate_install(shared_state, d)
    if shared_state['name'] == 'populate-sysroot':
        bb.build.exec_func('do_siteconfig', d)
        sstate_clean(shared_state, d)
        sstate_install(shared_state, d)
    sstate_package(shared_state, d)
}

EXPORT_FUNCTIONS do_siteconfig
