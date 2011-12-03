# The Boost web site provides free peer-reviewed portable
# C++ source libraries. The emphasis is on libraries which
# work well with the C++ Standard Library. The libraries are
# intended to be widely useful, and are in regular use by
# thousands of programmers across a broad spectrum of applications.
DESCRIPTION = "Free peer-reviewed portable C++ source libraries"
HOMEPAGE = "http://www.boost.org/"
SECTION = "libs"
DEPENDS = "boost-native zlib"
DEPENDS_virtclass-native = ""
LICENSE = "BSL-1 & MIT & Python-2.0"

ARM_INSTRUCTION_SET = "arm"

BOOST_VER = "${@"_".join(d.getVar("PV",1).split("."))}"
BOOST_MAJ = "${@"_".join(d.getVar("PV",1).split(".")[0:2])}"
BOOST_P = "boost_${BOOST_VER}"

INC_PR = "r1"

SRC_URI = "${SOURCEFORGE_MIRROR}/${BPN}/${BOOST_P}.tar.bz2"

S = "${WORKDIR}/${BOOST_P}"

BOOST_LIBS = "\
	date_time \
	filesystem \
	graph \
	iostreams \
	program_options \
	regex \
	signals \
	system \
	test \
	thread \
	"

# FIXME: for some reason this fails on powerpc
#BOOST_LIBS += "serialization"

# To enable python, uncomment the following:
#BOOST_LIBS += "python"
#DEPENDS += "python"
#PYTHON_ROOT = "${STAGING_DIR_HOST}/${prefix}"
#PYTHON_VERSION = "2.5"

# Make a package for each library, plus -dev
PACKAGES = "${PN}-dbg ${BOOST_PACKAGES}"
python __anonymous () {
    import bb

    packages = []
    extras = []
    for lib in d.getVar('BOOST_LIBS', 1).split( ):
            pkg = "boost-%s" % lib.replace("_", "-")
            extras.append("--with-%s" % lib)
            packages.append(pkg)
            if not d.getVar("FILES_%s" % pkg, 1):
                    d.setVar("FILES_%s" % pkg, "${libdir}/libboost_%s*.so.*" % lib)
    d.setVar("BOOST_PACKAGES", " ".join(packages))
    d.setVar("BJAM_EXTRA", " ".join(extras))
}

# Override the contents of specific packages
FILES_boost-serialization = "${libdir}/libboost_serialization*.so.* \
	${libdir}/libboost_wserialization*.so.*"
FILES_boost-test = "${libdir}/libboost_prg_exec_monitor*.so.* \
	${libdir}/libboost_unit_test_framework*.so.*"

# -dev last to pick up the remaining stuff
PACKAGES += "${PN}-dev"
FILES_${PN}-dev = "${includedir} ${libdir}/libboost_*.so ${libdir}/libboost_*.a"

# "boost" is a metapackage which pulls in all boost librabries
PACKAGES += "${PN}"
RRECOMMENDS_${PN} += "${BOOST_PACKAGES}"
ALLOW_EMPTY_${PN} = "1"

# to avoid GNU_HASH QA errors added LDFLAGS to ARCH; a little bit dirty but at least it works
TARGET_CC_ARCH += " ${LDFLAGS}" 

# Oh yippee, a new build system, it's sooo cooool I could eat my own
# foot.  inlining=on lets the compiler choose, I think.  At least this
# stuff is documented...
# NOTE: if you leave <debug-symbols>on then in a debug build the build sys
# objcopy will be invoked, and that won't work.  Building debug apparently
# requires hacking gcc-tools.jam
#
# Sometimes I wake up screaming.  Famous figures are gathered in the nightmare,
# Steve Bourne, Larry Wall, the whole of the ANSI C committee.  They're just
# standing there, waiting, but the truely terrifying thing is what they carry
# in their hands.  At first sight each seems to bear the same thing, but it is
# not so for the forms in their grasp are ever so slightly different one from
# the other.  Each is twisted in some grotesque way from the other to make each
# an unspeakable perversion impossible to perceive without the onset of madness.
# True insanity awaits anyone who perceives all of these horrors together.
#
# Quotation marks, there might be an easier way to do this, but I can't find
# it.  The problem is that the user.hpp configuration file must receive a
# pre-processor macro defined as the appropriate string - complete with "'s
# around it.  (<> is a possibility here but the danger to that is that the
# failure case interprets the < and > as shell redirections, creating
# random files in the source tree.)
#
#bjam: '-DBOOST_PLATFORM_CONFIG=\"config\"'
#do_compile: '-sGCC=... '"'-DBOOST_PLATFORM_CONFIG=\"config\"'"
SQD = '"'
EQD = '\"'
#boost.bb:   "...  '-sGCC=... '${SQD}'-DBOOST_PLATFORM_CONFIG=${EQD}config${EQD}'${SQD} ..."
BJAM_CONF = "${SQD}'-DBOOST_PLATFORM_CONFIG=${EQD}boost/config/platform/${TARGET_OS}.hpp${EQD}'${SQD}"

# bzip2 and zip are disabled because... they're broken - the compilation simply
# isn't working with bjam.  I guess they will fix it, but who needs it?  This
# only affects the (new in 33) iostream library.
BJAM_TOOLS   = "-sTOOLS=gcc \
		'-sGCC=${CC} '${BJAM_CONF} \
		'-sGXX=${CXX} '${BJAM_CONF} \
		'-sGCC_INCLUDE_DIRECTORY=${STAGING_INCDIR}' \
		'-sGCC_STDLIB_DIRECTORY=${STAGING_LIBDIR}' \
		'-sNO_BZIP2=1' \
		'-sNO_ZLIB=1' \
		'-sBUILD=release <optimization>space <inlining>on <debug-symbols>off' \
		'-sPYTHON_VERSION=${PYTHON_VERSION}' \
		'--layout=system' \
		"

BJAM_OPTS    = '${BJAM_TOOLS} \
		--builddir=${S}/${TARGET_SYS} \
		--with-python-root=${PYTHON_ROOT} \
		${BJAM_EXTRA}'


do_boostconfig() {
	cp -f boost/config/platform/linux.hpp boost/config/platform/linux-gnueabi.hpp

	echo 'using gcc : 4.3.1 : ${CXX} : compileflags -DBOOST_SP_USE_PTHREADS -I${includedir} linkflags -L${libdir} ;' >> ${S}/tools/build/v2/user-config.jam
}

addtask do_boostconfig after do_patch before do_configure

do_compile() {
	set -ex
	bjam ${BJAM_OPTS} --prefix=${prefix} \
		--exec-prefix=${exec_prefix} \
		--libdir=${libdir} \
		--includedir=${includedir}
}

do_install() {
	set -ex
	bjam ${BJAM_OPTS} \
		--libdir=${D}${libdir} \
		--includedir=${D}${includedir} \
		install
}

BBCLASSEXTEND = "native"

do_configure_virtclass-native() {
	:
}

do_boostconfig_virtclass-native() {
	:
}

do_compile_virtclass-native() {
	set -ex
	cd ${S}/tools/build/v2/engine
	rm -rf bin.*
	./build.sh gcc
}

# This is too terrible - the build script doesn't give any good
# way I can see to find out where the binaries are placed, so
# rely on only one bin.foo directory being created.
do_install_virtclass-native () {
	set -ex
	cd ${S}/tools/build/v2/engine
	install -d ${D}${bindir}/
	install -c -m 755 bin.*/bjam ${D}${bindir}/
}


