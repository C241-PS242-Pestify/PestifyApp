package com.learning.pestifyapp.ui.screen.navigation


import CameraScreen
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.learning.pestifyapp.MainActivity
import com.learning.pestifyapp.data.repository.UserRepository
import com.learning.pestifyapp.di.factory.HomeFactory
import com.learning.pestifyapp.di.factory.PespediaFactory
import com.learning.pestifyapp.di.factory.ViewModelFactory
import com.learning.pestifyapp.ui.components.BottomBarState
import com.learning.pestifyapp.ui.screen.authentication.forgotpassword.ForgotPasswordScreen
import com.learning.pestifyapp.ui.screen.authentication.forgotpassword.ForgotPasswordScreenViewModel
import com.learning.pestifyapp.ui.screen.authentication.login.LoginScreenViewModel
import com.learning.pestifyapp.ui.screen.authentication.login.LoginScreen
import com.learning.pestifyapp.ui.screen.authentication.register.RegisterScreen
import com.learning.pestifyapp.ui.screen.authentication.register.RegisterScreenViewModel
import com.learning.pestifyapp.ui.screen.authentication.register.UsernameScreen
import com.learning.pestifyapp.ui.screen.dashboard.detail.DetailArticleScreen
import com.learning.pestifyapp.ui.screen.dashboard.detail.DetailCategoriesScreen
import com.learning.pestifyapp.ui.screen.dashboard.detail.DetailEnsScreen
import com.learning.pestifyapp.ui.screen.dashboard.detail.DetailPlantScreen
import com.learning.pestifyapp.ui.screen.dashboard.ensiklopedia.EnsiklopediaScreen
import com.learning.pestifyapp.ui.screen.dashboard.history.HistoryScreen
import com.learning.pestifyapp.ui.screen.dashboard.home.HomeScreen
import com.learning.pestifyapp.ui.screen.dashboard.pescan.PescanScreen
import com.learning.pestifyapp.ui.screen.dashboard.pescan.PescanScreenViewModel
import com.learning.pestifyapp.ui.screen.dashboard.profile.PrivacyScreen
import com.learning.pestifyapp.ui.screen.dashboard.profile.ProfileScreen
import com.learning.pestifyapp.ui.screen.onboarding.OnboardingScreen
import com.learning.pestifyapp.ui.screen.splashscreen.SplashScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    context: MainActivity,
    bottomBarState: BottomBarState
) {
    val userRepository = UserRepository(context)

    NavHost(
        navController = navController,
        startDestination = Graph.SPLASH,
        modifier = Modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {

        composable(
            route = Graph.SPLASH,
            enterTransition = {
                fadeIn(animationSpec = tween(300, easing = LinearEasing))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300, easing = LinearEasing))
            }
        ) {

            SplashScreen(
                navController = navController,
                context = context,
            )
        }

        composable(
            route = Graph.ONBOARDING,
            enterTransition = {
                fadeIn(animationSpec = tween(300, easing = LinearEasing))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300, easing = LinearEasing))
            }
        ) {
            OnboardingScreen(
                navController = navController,
                context = context
            )
        }

        // |||||||||||||||||||||=== LOGIN/REGISTER ===||||||||||||||||||||||||||||||||||

        composable(
            route = Graph.LOGIN,
            enterTransition = {
                fadeIn(animationSpec = tween(300, easing = LinearEasing))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300, easing = LinearEasing))
            }
        ) {
            val loginScreenViewModel: LoginScreenViewModel =
                viewModel(factory = ViewModelFactory(userRepository))

            LoginScreen(
                navController = navController,
                context = context,
                viewModel = loginScreenViewModel
            )
        }

        composable(
            route = Graph.REGISTER,
            enterTransition = {
                fadeIn(animationSpec = tween(300, easing = LinearEasing))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300, easing = LinearEasing))
            }
        ) {
            val registerViewModel: RegisterScreenViewModel = viewModel(
                key = "RegisterScreenViewModel",
                factory = ViewModelFactory(userRepository)
            )
            RegisterScreen(
                navController = navController,
                context = context,
                viewModel = registerViewModel
            )
        }

        composable(
            route = Graph.USERNAME,
            enterTransition = {
                fadeIn(animationSpec = tween(300, easing = LinearEasing))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300, easing = LinearEasing))
            }
        ) {
            val registerViewModel: RegisterScreenViewModel = viewModel(
                key = "RegisterScreenViewModel",
                factory = ViewModelFactory(userRepository)
            )
            UsernameScreen(
                navController = navController,
                context = context,
                viewModel = registerViewModel
            )
        }



        composable(
            route = Graph.FORGOT_PASSWORD,
            enterTransition = {
                fadeIn(animationSpec = tween(300, easing = LinearEasing))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300, easing = LinearEasing))
            }
        ) {
            val forgotPasswordScreenViewModel: ForgotPasswordScreenViewModel =
                viewModel(factory = ViewModelFactory(userRepository))
            ForgotPasswordScreen(
                navController = navController,
                context = context,
                viewModel = forgotPasswordScreenViewModel
            )
        }

        // |||||||||||||||||||||=== SCAN ===||||||||||||||||||||||||||||||||||

        composable(route = Graph.CAMERA) {
            val pescanScreenViewModel: PescanScreenViewModel =
                viewModel(factory = ViewModelFactory(userRepository))
            CameraScreen(
                navController = navController,
                context = context,
                viewModel = pescanScreenViewModel
            )
        }

        // |||||||||||||||||||||=== HOME DASHBOARD ===||||||||||||||||||||||||||||||||||

        composable(
            route = Screen.Home.route,
            enterTransition = {
                if (initialState.destination.route == Screen.Home.route && targetState.destination.route?.startsWith(
                        "detail"
                    ) == true
                ) {
                    fadeIn(animationSpec = tween(300, easing = LinearEasing))
                } else {
                    null
                }
            },
            exitTransition = {
                if (initialState.destination.route == Screen.Home.route && targetState.destination.route?.startsWith(
                        "detail"
                    ) == true
                ) {
                    fadeOut(animationSpec = tween(300, easing = LinearEasing))
                } else {
                    null
                }
            }
        ) {
            HomeScreen(
                navController = navController,
                context = context,
                viewModel = viewModel(factory = HomeFactory.getInstance(MainActivity.CONTEXT)),
                bottomBarState = bottomBarState
            )
        }

        composable(
            route = Screen.Ensiklopedia.route,
            enterTransition = {
                fadeIn(animationSpec = tween(300, easing = LinearEasing))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300, easing = LinearEasing))
            }
        ) {
            EnsiklopediaScreen(
                navController = navController,
                viewModel = viewModel(factory = PespediaFactory.getInstance()),
                bottomBarState = bottomBarState
            )
        }

        composable(
            route = Screen.Pescan.route,
            enterTransition = {
                fadeIn(animationSpec = tween(300, easing = LinearEasing))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300, easing = LinearEasing))
            }
        ) {
            val pescanScreenViewModel: PescanScreenViewModel =
                viewModel(factory = ViewModelFactory(userRepository))
            PescanScreen(
                navController = navController,
                context = context,
                viewModel = pescanScreenViewModel
            )
        }

        composable(
            route = Screen.History.route,
        ) {
            HistoryScreen(
            )
        }

        composable(
            route = Screen.Profile.route,
            enterTransition = {
                fadeIn(animationSpec = tween(300, easing = LinearEasing))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300, easing = LinearEasing))
            }
        ) {
            ProfileScreen(
                navController = navController,
                context = context,
                viewModel = viewModel(factory = ViewModelFactory(userRepository))
            )
        }

        // |||||||||||||||||||||=== DETAIL ===||||||||||||||||||||||||||||||||||

        composable(
            route = Screen.DetailPlant.route,
            arguments = listOf(
                navArgument("plantId") {
                    type = NavType.StringType
                }
            ),
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) {
            val id = it.arguments?.getString("plantId") ?: ""
            DetailPlantScreen(
                plantId = id,
                navigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable(
            route = Screen.DetailEns.route,
            arguments = listOf(
                navArgument("ensId") {
                    type = NavType.StringType
                }
            ),
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) {
            val id = it.arguments?.getString("ensId") ?: ""
            DetailEnsScreen(
                ensId = id,
                navigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable(
            route = Screen.DetailArticle.route,
            arguments = listOf(
                navArgument("articleId") {
                    type = NavType.StringType
                }
            ),
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) {
            val id = it.arguments?.getString("articleId") ?: ""
            DetailArticleScreen(
                articleId = id,
                navigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable(
            route = Screen.DetailCategories.route,
            arguments = listOf(
                navArgument("category") {
                    type = NavType.StringType
                }
            ),
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) {
            val category = it.arguments?.getString("category") ?: ""
            DetailCategoriesScreen(
                selectedCategory = category,
                navigateBack = {
                    navController.navigateUp()
                },
                navigateToDetail = { articleId ->
                    navController.navigate(
                        Screen.DetailArticle.createRoute(
                            articleId
                        )
                    )
                }
            )
        }



        composable(route = Graph.PRIVACY) {
            PrivacyScreen(
                navController = navController,
                context = context,
                viewModel = viewModel(factory = ViewModelFactory(userRepository))

            )
        }

//        composable(route = Graph.DASHBOARD) {
//            dashBoard(
//                navController = navController,
//                context = context,
//            )
//        }

    }
}