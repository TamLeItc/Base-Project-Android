Base Module

### Core
    - BaseActivity
    - BaseFragment
    - BaseListAdapter
    - BaseView
    - BaseViewModel

### Interfaces
    IBaseView
    IBaseComponent

### Overrides functions
    getVB -> Provide View Binding
    initialize -> Load data, config theme/style, config onBackpressed, savedInstanceState
    initViews -> setColor/setText/setBackground/setInitData/... for views
    addListenerForViews -> set listener, set event for views
    bindViewModel -> listener state changed from viewModel
    