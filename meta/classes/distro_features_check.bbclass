# Allow checking of required and conflicting DISTRO_FEATURES
#
# REQUIRED_DISTRO_FEATURES: ensure every item on this list is included
#                           in DISTRO_FEATURES.
# CONFLICT_DISTRO_FEATURES: ensure no item in this list is included in
#                           DISTRO_FEATURES.
#
# Copyright 2013 (C) O.S. Systems Software LTDA.

python () {
    required_distro_features = d.getVar('REQUIRED_DISTRO_FEATURES', True)
    if required_distro_features:
        required_distro_features = required_distro_features.split()
        distro_features = (d.getVar('DISTRO_FEATURES', True) or "").split()
        for f in required_distro_features:
            if f in distro_features:
                continue
            else:
                raise bb.parse.SkipPackage("missing required distro feature '%s' (not in DISTRO_FEATURES)" % f)

    conflict_distro_features = d.getVar('CONFLICT_DISTRO_FEATURES', True)
    if conflict_distro_features:
        conflict_distro_features = conflict_distro_features.split()
        distro_features = (d.getVar('DISTRO_FEATURES', True) or "").split()
        for f in conflict_distro_features:
            if f in distro_features:
                raise bb.parse.SkipPackage("conflicting distro feature '%s' (in DISTRO_FEATURES)" % f)
}
