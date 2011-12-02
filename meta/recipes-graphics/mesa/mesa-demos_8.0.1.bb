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

PR = "r2"

SRC_URI = "ftp://ftp.freedesktop.org/pub/mesa/demos/${PV}/${BPN}-${PV}.tar.bz2 \
	file://dso_linking_change_build_fix.patch"

inherit autotools pkgconfig

SRC_URI[md5sum] = "320c2a4b6edc6faba35d9cb1e2a30bf4"
SRC_URI[sha256sum] = "4bc7f2b20d17e3eebfec288f2367a435cd2db71fc5ac9ece2c14827e290d77d1"
