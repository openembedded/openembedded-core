#
# Copyright (C) 2007 OpenedHand Ltd.
#

DESCRIPTION = "Software Development Tasks for OpenedHand Poky"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
DEPENDS = "task-core-console"
PR = "r7"

ALLOW_EMPTY = "1"
#PACKAGEFUNCS =+ 'generate_sdk_pkgs'

PACKAGES = "\
    task-core-sdk \
    task-core-sdk-dbg \
    task-core-sdk-dev"

RDEPENDS_task-core-sdk = "\
    autoconf \
    automake \
    binutils \
    binutils-symlinks \
    coreutils \
    cpp \
    cpp-symlinks \
    ccache \
    diffutils \
    gcc \
    gcc-symlinks \
    g++ \
    g++-symlinks \
    gettext \
    make \
    intltool \
    libstdc++ \
    libstdc++-dev \
    libtool \
    perl-module-re \
    perl-module-text-wrap \
    pkgconfig \
    findutils \
    quilt \
    less \
    distcc \
    ldd \
    file \
    tcl"

#python generate_sdk_pkgs () {
#    poky_pkgs = read_pkgdata('task-core', d)['PACKAGES']
#    pkgs = bb.data.getVar('PACKAGES', d, 1).split()
#    for pkg in poky_pkgs.split():
#        newpkg = pkg.replace('task-core', 'task-core-sdk')
#
#        # for each of the task packages, add a corresponding sdk task
#        pkgs.append(newpkg)
#
#        # for each sdk task, take the rdepends of the non-sdk task, and turn
#        # that into rrecommends upon the -dev versions of those, not unlike
#        # the package depchain code
#        spkgdata = read_subpkgdata(pkg, d)
#
#        rdepends = explode_deps(spkgdata.get('RDEPENDS_%s' % pkg) or '')
#        rreclist = []
#
#        for depend in rdepends:
#            split_depend = depend.split(' (')
#            name = split_depend[0].strip()
#            if packaged('%s-dev' % name, d):
#                rreclist.append('%s-dev' % name)
#            else:
#                deppkgdata = read_subpkgdata(name, d)
#                rdepends2 = explode_deps(deppkgdata.get('RDEPENDS_%s' % name) or '')
#                for depend in rdepends2:
#                    split_depend = depend.split(' (')
#                    name = split_depend[0].strip()
#                    if packaged('%s-dev' % name, d):
#                        rreclist.append('%s-dev' % name)
#
#            oldrrec = bb.data.getVar('RRECOMMENDS_%s' % newpkg, d) or ''
#            bb.data.setVar('RRECOMMENDS_%s' % newpkg, oldrrec + ' ' + ' '.join(rreclist), d)
#            # bb.note('RRECOMMENDS_%s = "%s"' % (newpkg, bb.data.getVar('RRECOMMENDS_%s' % newpkg, d)))
#
#    # bb.note('pkgs is %s' % pkgs)
#    bb.data.setVar('PACKAGES', ' '.join(pkgs), d)
#}
#
#PACKAGES_DYNAMIC = "task-core-sdk-*"
