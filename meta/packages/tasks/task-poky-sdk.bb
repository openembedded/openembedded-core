#
# Copyright (C) 2007 OpenedHand Ltd.
#

DESCRIPTON = "Software Development Tasks for OpenedHand Poky"
DEPENDS = "task-poky"
PR = "r2"

ALLOW_EMPTY = "1"
#PACKAGEFUNCS =+ 'generate_sdk_pkgs'

PACKAGES = "\
    task-poky-sdk \
    task-poky-sdk-dbg \
    task-poky-sdk-dev"

RDEPENDS_task-poky-sdk = "\
    autoconf \
    automake \
    binutils \
    binutils-symlinks \
    coreutils \
    cpp \
    cpp-symlinks \
    diffutils \
    gcc \
    gcc-symlinks \
    g++ \
    g++-symlinks \
    gettext \
    make \
    intltool \
    libgcc-dev \
    libstdc++ \
    libstdc++-dev \
    perl-module-re \
    perl-module-text-wrap \
    pkgconfig \
    quilt \
    less \
    distcc"

#python generate_sdk_pkgs () {
#    poky_pkgs = read_pkgdata('task-poky', d)['PACKAGES']
#    pkgs = bb.data.getVar('PACKAGES', d, 1).split()
#    for pkg in poky_pkgs.split():
#        newpkg = pkg.replace('task-poky', 'task-poky-sdk')
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
#PACKAGES_DYNAMIC = "task-poky-sdk-*"
