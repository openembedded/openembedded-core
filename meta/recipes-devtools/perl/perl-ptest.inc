inherit ptest

SRC_URI += "file://run-ptest \
           "

do_install_ptest () {
	mkdir -p ${D}${PTEST_PATH}
	cp -pv TestInit.pm MANIFEST config.sh ${D}${PTEST_PATH}/

	tar -cf - t/ | ( cd ${D}${PTEST_PATH} && tar -xf - )
	ln -sf ${bindir}/perl ${D}${PTEST_PATH}/t/
	ln -sf ${libdir}/perl/${PV} ${D}${PTEST_PATH}/lib

	for dir in `find ext/ dist/ cpan/ -maxdepth 2 -type d -name t ` ; do
	    tar -cf - $dir | ( cd ${D}${PTEST_PATH} && tar -xf - )
	done
	for file in `find ext dist cpan -name \*.t -o -name \test.pl`;  do
	    tar -cf - $file | ( cd ${D}${PTEST_PATH} && tar -xf - )
	done

	# Tweaks to make tests pass
	cp -pv lib/unicore/TestProp.pl ${D}${libdir}/perl/${PV}/unicore/
	# Put all *.t files from the lib dir in the ptest package
	cd lib
	for file in `find -name \*.t`; do
	    tar -cf - $file | ( cd ${D}${libdir}/perl/${PV} && tar -xf - )
	done
	cd ..

	mkdir -p ${D}${libdir}/perl/${PV}/XS
	cp -pv lib/XS/APItest.pm ${D}${libdir}/perl/${PV}/XS/
	cp -pv lib/XS/Typemap.pm ${D}${libdir}/perl/${PV}/XS/
	mkdir -p ${D}${libdir}/perl/${PV}/auto/XS/APItest
	cp -pv lib/auto/XS/APItest/APItest.so ${D}${libdir}/perl/${PV}/auto/XS/APItest/
	mkdir -p ${D}${libdir}/perl/${PV}/auto/XS/Typemap
	cp -pv lib/auto/XS/Typemap/Typemap.so ${D}${libdir}/perl/${PV}/auto/XS/Typemap/
	cp -pv cpan/Digest-MD5/README ${D}${PTEST_PATH}/cpan/Digest-MD5/
	cp -pv cpan/Digest-MD5/MD5.xs ${D}${PTEST_PATH}/cpan/Digest-MD5/
}

python populate_packages_prepend() {
    # Put all *.t files from the lib dir in the ptest package
    # do_split_packages requires a pair of () in the regex, but we have nothing
    # to match, so use an empty pair.
    if oe.utils.contains('DISTRO_FEATURES', 'ptest', True, False, d):
        do_split_packages(d, d.expand('${libdir}/perl/${PV}'), '.*\.t()',
            '${PN}-ptest%s', '%s', recursive=True, match_path=True)
}

RDEPENDS_${PN}-ptest += "${PN}-modules ${PN}-doc ${PN}-misc"
