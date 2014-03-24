# Filter the license, the copyleft_should_include returns True for the
# COPYLEFT_LICENSE_INCLUDE recipe, and False for the
# COPYLEFT_LICENSE_EXCLUDE.
#
# By default, includes all GPL and LGPL, and excludes CLOSED and Proprietary.
#
# vi:sts=4:sw=4:et

COPYLEFT_LICENSE_INCLUDE ?= 'GPL* LGPL*'
COPYLEFT_LICENSE_INCLUDE[type] = 'list'
COPYLEFT_LICENSE_INCLUDE[doc] = 'Space separated list of globs which include licenses'

COPYLEFT_LICENSE_EXCLUDE ?= 'CLOSED Proprietary'
COPYLEFT_LICENSE_EXCLUDE[type] = 'list'
COPYLEFT_LICENSE_EXCLUDE[doc] = 'Space separated list of globs which exclude licenses'

COPYLEFT_RECIPE_TYPE ?= '${@copyleft_recipe_type(d)}'
COPYLEFT_RECIPE_TYPE[doc] = 'The "type" of the current recipe (e.g. target, native, cross)'

COPYLEFT_RECIPE_TYPES ?= 'target'
COPYLEFT_RECIPE_TYPES[type] = 'list'
COPYLEFT_RECIPE_TYPES[doc] = 'Space separated list of recipe types to include'

COPYLEFT_AVAILABLE_RECIPE_TYPES = 'target native nativesdk cross crosssdk cross-canadian'
COPYLEFT_AVAILABLE_RECIPE_TYPES[type] = 'list'
COPYLEFT_AVAILABLE_RECIPE_TYPES[doc] = 'Space separated list of available recipe types'

def copyleft_recipe_type(d):
    for recipe_type in oe.data.typed_value('COPYLEFT_AVAILABLE_RECIPE_TYPES', d):
        if oe.utils.inherits(d, recipe_type):
            return recipe_type
    return 'target'

def copyleft_should_include(d):
    """
    Determine if this recipe's sources should be deployed for compliance
    """
    import ast
    import oe.license
    from fnmatch import fnmatchcase as fnmatch

    recipe_type = d.getVar('COPYLEFT_RECIPE_TYPE', True)
    if recipe_type not in oe.data.typed_value('COPYLEFT_RECIPE_TYPES', d):
        return False, 'recipe type "%s" is excluded' % recipe_type

    include = oe.data.typed_value('COPYLEFT_LICENSE_INCLUDE', d)
    exclude = oe.data.typed_value('COPYLEFT_LICENSE_EXCLUDE', d)

    try:
        is_included, reason = oe.license.is_included(d.getVar('LICENSE', True), include, exclude)
    except oe.license.LicenseError as exc:
        bb.fatal('%s: %s' % (d.getVar('PF', True), exc))
    else:
        if is_included:
            if reason:
                return True, 'recipe has included licenses: %s' % ', '.join(reason)
            else:
                return False, 'recipe does not include a copyleft license'
        else:
            return False, 'recipe has excluded licenses: %s' % ', '.join(reason)


