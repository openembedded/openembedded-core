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
PACKAGE_ARCH = "all"

# This automatically adds -dbg and -dev flavours of all PACKAGES
# to the list. Their dependencies (RRECOMMENDS) are handled as usual
# by package_depchains in a following step.
python () {
    packages = d.getVar('PACKAGES', True).split()
    genpackages = []
    for pkg in packages:
        for postfix in ['-dbg', '-dev']:
            genpackages.append(pkg+postfix)
    d.setVar('PACKAGES', ' '.join(packages+genpackages))
}

# We don't want to look at shared library dependencies for the
# dbg packages
DEPCHAIN_DBGDEFAULTDEPS = "1"
