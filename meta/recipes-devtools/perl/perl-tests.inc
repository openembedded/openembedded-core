PACKAGES += "perl-tests"
PERL_TEST_DIR = "/opt/perl-tests"

do_install_append () {
	mkdir -p ${D}${PERL_TEST_DIR}
	cp -pv TestInit.pm MANIFEST config.sh ${D}${PERL_TEST_DIR}/

	tar -cf - t/ | ( cd ${D}${PERL_TEST_DIR} && tar -xf - )
	ln -sf ${bindir}/perl ${D}${PERL_TEST_DIR}/t/
	ln -sf ${libdir}/perl/${PV} ${D}${PERL_TEST_DIR}/lib

	for dir in `find ext/ dist/ cpan/ -maxdepth 2 -type d -name t ` ; do
	    tar -cf - $dir | ( cd ${D}${PERL_TEST_DIR} && tar -xf - )
	done
	for file in `find ext dist cpan -name \*.t -o -name \test.pl`;  do
	    tar -cf - $file | ( cd ${D}${PERL_TEST_DIR} && tar -xf - )
	done

	# Tweaks to make tests pass
	cp -pv lib/unicore/TestProp.pl ${D}${libdir}/perl/${PV}/unicore/
	cp -pv lib/AnyDBM_File.t ${D}${libdir}/perl/${PV}/

	mkdir -p ${D}${libdir}/perl/${PV}/XS
	cp -pv lib/XS/APItest.pm ${D}${libdir}/perl/${PV}/XS/
	cp -pv lib/XS/Typemap.pm ${D}${libdir}/perl/${PV}/XS/
	mkdir -p ${D}${libdir}/perl/${PV}/auto/XS/APItest
	cp -pv lib/auto/XS/APItest/APItest.so ${D}${libdir}/perl/${PV}/auto/XS/APItest/
	mkdir -p ${D}${libdir}/perl/${PV}/auto/XS/Typemap
	cp -pv lib/auto/XS/Typemap/Typemap.so ${D}${libdir}/perl/${PV}/auto/XS/Typemap/
	cp -pv cpan/Digest-MD5/README ${D}${PERL_TEST_DIR}/cpan/Digest-MD5/
	cp -pv cpan/Digest-MD5/MD5.xs ${D}${PERL_TEST_DIR}/cpan/Digest-MD5/
}

FILES_${PN}-tests = "${PERL_TEST_DIR} \
                     ${libdir}/perl/${PV}/AnyDBM_File.t"
RDEPENDS_${PN}-tests = "${PN}-modules ${PN}-doc ${PN}-misc"
