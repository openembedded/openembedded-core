
BASE_SRC_URI = "ftp://ftp.software.ibm.com/software/globalization/icu/3.6/icu4c-3_6-src.tgz \
           file://gccfix.patch;patch=1 \
           file://use-g++-for-linking.patch;patch=1"
SRC_URI = "${BASE_SRC_URI} \
           file://noldlibpath.patch;patch=1"
SRC_URI_virtclass-native = "${BASE_SRC_URI}"

LICENSE = "ICU"
DEPENDS = "icu-native"
DEPENDS_virtclass-native = ""

S = "${WORKDIR}/icu/source"

PARALLEL_MAKE = ""
FULL_OPTIMIZATION_arm = "-Os"

inherit autotools pkgconfig binconfig

do_configure() {
	libtoolize --force
	gnu-configize --force
	oe_runconf
	if [ "${BUILD_ARCH}" != "${HOST_ARCH}" ]; then
		# In the non-native case we need to make substitutions to use
		# the native versions of the tools
		for i in */Makefile */*.inc */*/Makefile */*/*.inc ; do
			sed -i -e 's:$(INVOKE) $(BINDIR)/:$(INVOKE) :g' $i 
			sed -i -e 's:$(BINDIR)/::g' $i 
		done
		sed -i -e 's:$(BINDIR)/::g' extra/uconv/pkgdata.inc || true
		sed -i -e 's:$(BINDIR)/::g' extra/uconv/pkgdata.inc.in || true
	fi
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

BBCLASSEXTEND = "native"
