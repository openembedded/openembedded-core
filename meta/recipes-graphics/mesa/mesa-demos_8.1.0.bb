SUMMARY = "Mesa demo applications"
DESCRIPTION = "This package includes the demonstration application, such as glxgears. \
These applications can be used for Mesa validation and benchmarking."
HOMEPAGE = "http://mesa3d.org"
BUGTRACKER = "https://bugs.freedesktop.org"
SECTION = "x11"

LICENSE = "MIT & PD"
LIC_FILES_CHKSUM = "file://src/xdemos/glxgears.c;beginline=1;endline=20;md5=914225785450eff644a86c871d3ae00e \
                    file://src/xdemos/glxdemo.c;beginline=1;endline=8;md5=b01d5ab1aee94d35b7efaa2ef48e1a06"

DEPENDS = "virtual/libx11 virtual/libgl glew"

SRC_URI = "ftp://ftp.freedesktop.org/pub/mesa/demos/${PV}/${BPN}-${PV}.tar.bz2 \
        file://glut.patch \
        file://egl-mesa-screen-surface-build-fix.patch \
        file://egl-mesa-screen-surface-query.patch"

SRC_URI[md5sum] = "9df33ba69a26bbfbc7c8148602e59542"
SRC_URI[sha256sum] = "9703fa0646b32a1e68d2abf5628f936f77bf97c69ffcaac90de380820a87a828"

inherit autotools pkgconfig

EXTRA_OECONF = "--without-glut"
