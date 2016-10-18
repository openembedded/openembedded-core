# Copyright (C) 2005, Koninklijke Philips Electronics NV.  All Rights Reserved
# Released under the MIT license (see packages/COPYING)

SUMMARY = "Cross-platform, open-source make system"
HOMEPAGE = "http://www.cmake.org/"
BUGTRACKER = "http://public.kitware.com/Bug/my_view_page.php"
SECTION = "console/utils"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://Copyright.txt;md5=052f86c15bbde68af55c7f7b340ab639 \
                    file://Source/cmake.h;beginline=1;endline=10;md5=341736dae83c9e344b53eeb1bc7d7bc2"

CMAKE_MAJOR_VERSION = "${@'.'.join(d.getVar('PV', True).split('.')[0:2])}"

SRC_URI = "https://cmake.org/files/v${CMAKE_MAJOR_VERSION}/cmake-${PV}.tar.gz \
           file://support-oe-qt4-tools-names.patch \
           file://qt4-fail-silent.patch \
           file://avoid-gcc-warnings-with-Wstrict-prototypes.patch \
           "

SRC_URI[md5sum] = "139d7affdd4e8ab1edfc9f4322d69e43"
SRC_URI[sha256sum] = "189ae32a6ac398bb2f523ae77f70d463a6549926cde1544cd9cc7c6609f8b346"

UPSTREAM_CHECK_REGEX = "cmake-(?P<pver>\d+(\.\d+)+)\.tar"

# Ugly hack to work around undefined OE_QMAKE_PATH_EXTERNAL_HOST_BINS variable
# and possibly missing qmake binary (qtbase-native can be removed from sysroot
# e.g. in order to upgrade it, even when there is target qtbase)

# Fixes errors like this in cmake(-native).do_configure:
#| -- Performing Test run_pic_test - Success
#| CMake Error at tmp-eglibc/sysroots/qemuarm/usr/lib/cmake/Qt5Core/Qt5CoreConfig.cmake:27 (message):
#|   The imported target "Qt5::Core" references the file
#|
#|      "/qmake"
#|
#|   but this file does not exist.  Possible reasons include:

do_configure_prepend() {
	sed -i 's/^find_package(Qt5Core QUIET)$/#find_package(Qt5Core QUIET)/g' ${S}/Tests/RunCMake/CMakeLists.txt
	sed -i 's/^  find_package(Qt5Core REQUIRED)/#  find_package(Qt5Core REQUIRED)/g' ${S}/Tests/Qt4And5Automoc/CMakeLists.txt
	sed -i 's/^  find_package(Qt5Widgets QUIET NO_MODULE)/#  find_package(Qt5Widgets QUIET NO_MODULE)/g' ${S}/Tests/CMakeLists.txt
	sed -i 's/^find_package(Qt5Widgets QUIET)/#find_package(Qt5Widgets QUIET)/g' ${S}/Source/QtDialog/CMakeLists.txt
	sed -i 's/^  find_package(Qt5Widgets REQUIRED)/#  find_package(Qt5Widgets REQUIRED)/g' ${S}/Tests/QtAutoUicInterface/CMakeLists.txt
	sed -i 's/^  find_package(Qt5Widgets REQUIRED)/#  find_package(Qt5Widgets REQUIRED)/g' ${S}/Tests/QtAutogen/CMakeLists.txt
	sed -i 's/^  find_package(Qt5Core REQUIRED)/#  find_package(Qt5Core REQUIRED)/g' ${S}/Tests/QtAutogen/autorcc_depends/CMakeLists.txt
}
