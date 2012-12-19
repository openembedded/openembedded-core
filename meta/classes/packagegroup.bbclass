# Class for packagegroup (package group) recipes

# packagegroup packages are only used to pull in other packages
# via their dependencies. They are empty.
ALLOW_EMPTY = "1"

# By default, only the packagegroup package itself is in PACKAGES.
# -dbg and -dev flavours are handled by the anonfunc below.
# This means that packagegroup recipes used to build multiple packagegroup
# packages have to modify PACKAGES after inheriting packagegroup.bbclass.
PACKAGES = "${PN}"

# By default, packagegroup packages do not depend on a certain architecture.
# Only if dependencies are modified by MACHINE_FEATURES, packages
# need to be set to MACHINE_ARCH after inheriting packagegroup.bbclass
inherit allarch

# This automatically adds -dbg and -dev flavours of all PACKAGES
# to the list. Their dependencies (RRECOMMENDS) are handled as usual
# by package_depchains in a following step.
python () {
    if d.getVar('PACKAGEGROUP_DISABLE_COMPLEMENTARY', True) == '1':
        return

    packages = d.getVar('PACKAGES', True).split()
    genpackages = []
    for pkg in packages:
        for postfix in ['-dbg', '-dev', '-ptest']:
            genpackages.append(pkg+postfix)
    d.setVar('PACKAGES', ' '.join(packages+genpackages))
}

# We don't want to look at shared library dependencies for the
# dbg packages
DEPCHAIN_DBGDEFAULTDEPS = "1"

# We only need the packaging tasks - disable the rest
do_fetch[noexec] = "1"
do_unpack[noexec] = "1"
do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_install[noexec] = "1"
do_populate_sysroot[noexec] = "1"
