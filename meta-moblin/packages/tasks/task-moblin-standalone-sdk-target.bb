#
# Copyright (C) 2008-2010 Intel Corporation.
#

DESCRIPTION = "Target packages for the standalone Moblin SDK"
PR = "r6"
LICENSE = "MIT"
ALLOW_EMPTY = "1"

PACKAGES = "${PN} ${PN}-dbg"

RDEPENDS_${PN} = "\
    libgcc \
    libstdc++ \
    task-poky-standalone-sdk-target \
    dbus-dev \
    dbus-glib-dev \
    gtk+-dev \
    gstreamer-dev \
    bluez4-dev \
    gconf-dbus-dev \
    avahi-dev \
    telepathy-glib-dev \
    eds-dbus-dev \
    libecal-dev \
    libebook-dev \
    libglade-dev \
    libxi-dev \
    libsqlite-dev \
    clutter-1.0-dev \
    nbtk-dev \
    mutter-dev \
    mutter-moblin-dev \
    clutter-gst-0.10-dev \
    clutter-gtk-0.10-dev \
    clutter-imcontext \
    libccss \
    "

GLIBC_DEPENDENCIES = "\
    libsegfault \
    glibc \
    glibc-dbg \
    glibc-dev \
    glibc-utils \
    glibc-thread-db \
    glibc-localedata-i18n \
    glibc-gconv-ibm850 \
    glibc-gconv-cp1252 \
    glibc-gconv-iso8859-1 \
    glibc-gconv-iso8859-15 \
    locale-base-en-gb \
    "

RDEPENDS_${PN}_append_linux = "${GLIBC_DEPENDENCIES}"
RDEPENDS_${PN}_append_linux-gnueabi = "${GLIBC_DEPENDENCIES}"

UCLIBC_DEPENDENCIES = "\
    uclibc \
    uclibc-dbg \
    uclibc-dev \
    uclibc-thread-db \
    "

RDEPENDS_${PN}_append_linux-uclibc = "${UCLIBC_DEPENDENCIES}"
RDEPENDS_${PN}_append_linux-uclibcgnueabi = "${UCLIBC_DEPENDENCIES}"

