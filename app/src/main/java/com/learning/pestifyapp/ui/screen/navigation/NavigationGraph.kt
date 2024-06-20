package com.learning.pestifyapp.ui.screen.navigation


import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.learning.pestifyapp.MainActivity
import com.learning.pestifyapp.data.repository.HistoryRepository
import com.learning.pestifyapp.data.repository.UserRepository
import com.learning.pestifyapp.di.factory.HistoryFactory
import com.learning.pestifyapp.di.factory.HomeFactory
import com.learning.pestifyapp.di.factory.PespediaFactory
import com.learning.pestifyapp.di.factory.PestFactory
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
import com.learning.pestifyapp.ui.screen.dashboard.detail.DetailHistoryScreen
import com.learning.pestifyapp.ui.screen.dashboard.detail.DetailPlantScreen
import com.learning.pestifyapp.ui.screen.dashboard.ensiklopedia.EnsiklopediaScreen
import com.learning.pestifyapp.ui.screen.dashboard.history.HistoryScreen
import com.learning.pestifyapp.ui.screen.dashboard.history.HistoryViewModel
import com.learning.pestifyapp.ui.screen.dashboard.home.HomeScreen
import com.learning.pestifyapp.ui.screen.dashboard.pescan.CameraScreen
import com.learning.pestifyapp.ui.screen.dashboard.pescan.PestResultScreen
import com.learning.pestifyapp.ui.screen.dashboard.pescan.PestScreen
import com.learning.pestifyapp.ui.screen.dashboard.pescan.PestViewModel
import com.learning.pestifyapp.ui.screen.dashboard.profile.PrivacyScreen
import com.learning.pestifyapp.ui.screen.dashboard.profile.ProfileScreen
import com.learning.pestifyapp.ui.screen.onboarding.OnboardingScreen
import com.learning.pestifyapp.ui.screen.splashscreen.SplashScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    context: MainActivity,
    bottomBarState: BottomBarState,
) {
    val userRepository = UserRepository(context)

    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = Graph.SPLASH,
        contentAlignment = Alignment.TopStart,
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
            val pestViewModel: PestViewModel =
                viewModel(factory = PestFactory.getInstance(context))
            CameraScreen(
                navController = navController,
                context = context,
                viewModel = pestViewModel
            )
        }

        // |||||||||||||||||||||=== HOME DASHBOARD ===||||||||||||||||||||||||||||||||||

        composable(
            route = Screen.Home.route,
            enterTransition = {
                val isDetailToHome =
                    initialState.destination.route?.startsWith("detail") == true && targetState.destination.route == Screen.Home.route
                if (isDetailToHome) {
                    fadeIn(animationSpec = tween(300, easing = LinearEasing))
                } else {
                    fadeIn(animationSpec = tween(300, easing = LinearEasing))
                }
            },
            exitTransition = {
                val isHomeToDetail =
                    initialState.destination.route == Screen.Home.route && targetState.destination.route?.startsWith(
                        "detail"
                    ) == true
                if (isHomeToDetail) {
                    fadeOut(animationSpec = tween(500, easing = LinearEasing))
                } else {
                    fadeOut(animationSpec = tween(300, easing = LinearEasing))
                }
            }
        ) {
            HomeScreen(
                navController = navController,
                context = context,
                viewModel = viewModel(factory = HomeFactory.getInstance(context)),
                bottomBarState = bottomBarState
            )
        }

        composable(
            route = Screen.Ensiklopedia.route,
            enterTransition = {
                if (initialState.destination.route == Screen.Ensiklopedia.route && targetState.destination.route?.startsWith(
                        "detail"
                    ) == true
                ) {
                    fadeIn(animationSpec = tween(300, easing = LinearEasing))
                } else {
                    null
                }
            },
            exitTransition = {
                if (initialState.destination.route == Screen.Ensiklopedia.route && targetState.destination.route?.startsWith(
                        "detail"
                    ) == true
                ) {
                    fadeOut(animationSpec = tween(300, easing = LinearEasing))
                } else {
                    null
                }
            }
        ) {
            EnsiklopediaScreen(
                navController = navController,
                viewModel = viewModel(factory = PespediaFactory.getInstance(context)),
                bottomBarState = bottomBarState
            )
        }

        composable(
            route = Screen.Pescan.route,
            enterTransition = {
                if (initialState.destination.route == Screen.Pescan.route && targetState.destination.route?.startsWith(
                        "detail"
                    ) == true
                ) {
                    fadeIn(animationSpec = tween(300, easing = LinearEasing))
                } else {
                    null
                }
            },
            exitTransition = {
                if (initialState.destination.route == Screen.Pescan.route && targetState.destination.route?.startsWith(
                        "detail"
                    ) == true
                ) {
                    fadeOut(animationSpec = tween(300, easing = LinearEasing))
                } else {
                    null
                }
            }
        ) {
            PestScreen(
                navController = navController,
            )
        }

        composable(
            route = Screen.History.route,
            enterTransition = {
                if (initialState.destination.route == Screen.History.route && targetState.destination.route?.startsWith(
                        "detail"
                    ) == true
                ) {
                    fadeIn(animationSpec = tween(300, easing = LinearEasing))
                } else {
                    null
                }
            },
            exitTransition = {
                if (initialState.destination.route == Screen.History.route && targetState.destination.route?.startsWith(
                        "detail"
                    ) == true
                ) {
                    fadeOut(animationSpec = tween(300, easing = LinearEasing))
                } else {
                    null
                }
            }
        ) {
            HistoryScreen(
                navController = navController,
                viewModel = viewModel(factory = HistoryFactory.getInstance(context))
            )
        }

        composable(
            route = Screen.Profile.route,
            enterTransition = {
                if (initialState.destination.route == Screen.Profile.route && targetState.destination.route?.startsWith(
                        "detail"
                    ) == true
                ) {
                    fadeIn(animationSpec = tween(300, easing = LinearEasing))
                } else {
                    null
                }
            },
            exitTransition = {
                if (initialState.destination.route == Screen.Profile.route && targetState.destination.route?.startsWith(
                        "detail"
                    ) == true
                ) {
                    fadeOut(animationSpec = tween(300, easing = LinearEasing))
                } else {
                    null
                }
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
                },
                context = context
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
                },
                context = context
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
                },
                context = context
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
                        600, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(600, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Up
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        600, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(600, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.Down
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
                },
                context = context
            )
        }

        composable(route = "detail/{historyId}") { backStackEntry ->
            val historyId = backStackEntry.arguments?.getString("historyId")
            if (historyId != null) {
                val historyViewModel: HistoryViewModel = viewModel(
                    factory = HistoryFactory.getInstance(context)
                )
                DetailHistoryScreen(
                    historyId = historyId,
                    viewModel = historyViewModel,
                    navController = navController
                )
            }
        }

        composable(
            route = "result_screen/{imageUri}",
            arguments = listOf(navArgument("imageUri") { type = NavType.StringType })
        ) { backStackEntry ->
            val imageUri = backStackEntry.arguments?.getString("imageUri")?.toUri()
            PestResultScreen(
                viewModel(factory = PestFactory.getInstance(context)),
                navController = navController,
                context = context,
                imageUri = imageUri
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