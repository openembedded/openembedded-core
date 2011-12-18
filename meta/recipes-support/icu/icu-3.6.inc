SUMMARY = "International Component for Unicode libraries"
DESCRIPTION = "The International Component for Unicode (ICU) is a mature, portable set of C/C++ and Java libraries for Unicode support, software internationalization (I18N) and globalization (G11N), giving applications the same results on all platforms."
HOMEPAGE = "http://www-01.ibm.com/software/globalization/icu/index.jsp"

BASE_SRC_URI = "ftp://ftp.software.ibm.com/software/globalization/icu/3.6/icu4c-3_6-src.tgz \
           file://gccfix.patch;apply=yes \
           file://fix-parallel-build.patch \
           file://use-g++-for-linking.patch;apply=yes"
SRC_URI = "${BASE_SRC_URI} \
           file://noldlibpath.patch;apply=yes"
SRC_URI_virtclass-native = "${BASE_SRC_URI}"

LICENSE = "ICU"
LIC_FILES_CHKSUM = "file://../license.html;md5=5c94767cedb5d6987c902ac850ded2c6"
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
	if [ "${PN}" != "icu-native" ]; then
		# In the non-native case we need to make substitutions to use
		# the native versions of the tools
		for i in */Makefile* */*.inc */*/Makefile* */*/*.inc */*/*.inc.in ; do
			sed -i -e 's:$(INVOKE) $(BINDIR)/:$(INVOKE) :g' $i 
			sed -i -e 's:$(BINDIR)/::g' $i 
		done
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

SSTATE_SCAN_FILES += "Makefile.inc"
