require icu-3.6.inc

DEPENDS += "icu-native"
PR = "r3"

SRC_URI += "file://use-g++-for-linking.patch;patch=1"

do_configure_append() {
        for i in */Makefile */*.inc */*/Makefile */*/*.inc ; do
		sed -i -e 's:$(INVOKE) $(BINDIR)/:$(INVOKE) :g' $i 
		sed -i -e 's:$(BINDIR)/::g' $i 
        done
	sed -i -e 's:$(BINDIR)/::g' extra/uconv/pkgdata.inc || true
	sed -i -e 's:$(BINDIR)/::g' extra/uconv/pkgdata.inc.in || true
}

do_compile() {
	oe_runmake 'CXX=${CXX}'
}

do_install_append() {
        chmod +x ${D}${libdir}/lib*
}

PACKAGES =+ "libicudata libicuuc libicui18n libicule libiculx libicutu libicuio"

FILES_libicudata = "${libdir}/libicudata.so.*"
FILES_libicuuc = "${libdir}/libicuuc.so.*"
FILES_libicui18n = "${libdir}/libicui18n.so.*"
FILES_libicule = "${libdir}/libicule.so.*"
FILES_libiculx = "${libdir}/libiculx.so.*"
FILES_libicutu = "${libdir}/libicutu.so.*"
FILES_libicuio = "${libdir}/libicuio.so.*"

do_stage() {
        autotools_stage_all
}	


