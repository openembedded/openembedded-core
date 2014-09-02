SUMMARY = "OpenGL driver testing framework"
LICENSE = "MIT & LGPLv2+ & GPLv3 & GPLv2+ & BSD-3-Clause"
LIC_FILES_CHKSUM = "file://COPYING;md5=b2beded7103a3d8a442a2a0391d607b0"

SRC_URI = "git://anongit.freedesktop.org/piglit"

# From 2012/12/30.
SRCREV = "bbeff5d21b06d37338ad28e42d88f499bef13268"
# (when PV goes above 1.0 remove the trailing r)
PV = "1.0+gitr${SRCPV}"

S = "${WORKDIR}/git"

DEPENDS = "virtual/libx11 libxrender waffle virtual/libgl libglu python-mako-native python-numpy-native"

inherit cmake pythonnative

# As piglit doesn't install, enforce in-tree builds so that we can easily copy
# contents out of $S and $B.
B="${S}"

# CMake sets the rpath at build time with the source tree, and will reset it at
# install time. As we don't install this doesn't happen, so force the rpath to
# what we need.
EXTRA_OECMAKE = "-DCMAKE_BUILD_WITH_INSTALL_RPATH=1 -DCMAKE_INSTALL_RPATH=${libdir}/piglit/lib"

do_install() {
	install -d ${D}${bindir}
	install -m 0755 piglit-*.py ${D}${bindir}

	install -d ${D}${libdir}/piglit/

	install -d ${D}${libdir}/piglit/bin
	install -m 755 ${S}/bin/* ${D}${libdir}/piglit/bin

	cp -Pr lib/ ${D}${libdir}/piglit/
	cp -Pr framework/ ${D}${libdir}/piglit/
	cp -Pr generated_tests/ ${D}${libdir}/piglit/
	cp -Pr tests/ ${D}${libdir}/piglit/
	cp -Pr templates/ ${D}${libdir}/piglit/

	sed -i -e 's|sys.path.append(.*)|sys.path.append("${libdir}/piglit")|' ${D}${bindir}/piglit-*.py
	sed -i -e 's|^templatedir = .*$|templatedir = "${libdir}/piglit/templates"|' ${D}${bindir}/piglit-summary-html.py
}

FILES_${PN}-dbg += "${libdir}/piglit/*/.debug/"

RDEPENDS_${PN} = "python waffle python-json python-subprocess \
	python-multiprocessing python-textutils python-netserver python-shell \
	mesa-demos bash \
	"
